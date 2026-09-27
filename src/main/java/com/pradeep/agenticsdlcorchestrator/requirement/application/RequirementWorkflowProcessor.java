package com.pradeep.agenticsdlcorchestrator.requirement.application;

import com.pradeep.agenticsdlcorchestrator.requirement.agent.RequirementAnalysisValidator;
import com.pradeep.agenticsdlcorchestrator.requirement.agent.RequirementInterpreterAgent;
import com.pradeep.agenticsdlcorchestrator.requirement.domain.RequirementAnalysis;
import com.pradeep.agenticsdlcorchestrator.requirement.persistence.ClarificationQuestionEntity;
import com.pradeep.agenticsdlcorchestrator.requirement.persistence.ClarificationQuestionRepository;
import com.pradeep.agenticsdlcorchestrator.requirement.persistence.RequirementAnalysisEntity;
import com.pradeep.agenticsdlcorchestrator.requirement.persistence.RequirementAnalysisRepository;
import com.pradeep.agenticsdlcorchestrator.requirement.persistence.RequirementItemEntity;
import com.pradeep.agenticsdlcorchestrator.requirement.persistence.RequirementItemEntity.ItemType;
import com.pradeep.agenticsdlcorchestrator.requirement.persistence.RequirementItemRepository;
import com.pradeep.agenticsdlcorchestrator.requirement.persistence.RevisionOutputEntity;
import com.pradeep.agenticsdlcorchestrator.requirement.persistence.RevisionOutputEntity.InputDimension;
import com.pradeep.agenticsdlcorchestrator.requirement.persistence.RevisionOutputEntity.OutputStatus;
import com.pradeep.agenticsdlcorchestrator.requirement.persistence.RevisionOutputRepository;
import com.pradeep.agenticsdlcorchestrator.workflow.domain.WorkflowStatus;
import com.pradeep.agenticsdlcorchestrator.workflow.persistence.WorkflowRepository;
import com.pradeep.agenticsdlcorchestrator.workflow.persistence.WorkflowRevisionRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
public class RequirementWorkflowProcessor {
    private final RequirementInterpreterAgent interpreter;
    private final RequirementAnalysisValidator validator;
    private final WorkflowRepository workflows;
    private final WorkflowRevisionRepository revisions;
    private final RequirementAnalysisRepository analyses;
    private final RequirementItemRepository items;
    private final ClarificationQuestionRepository questions;
    private final RevisionOutputRepository outputs;

    public RequirementWorkflowProcessor(RequirementInterpreterAgent interpreter,
                                        RequirementAnalysisValidator validator,
                                        WorkflowRepository workflows,
                                        WorkflowRevisionRepository revisions,
                                        RequirementAnalysisRepository analyses,
                                        RequirementItemRepository items,
                                        ClarificationQuestionRepository questions,
                                        RevisionOutputRepository outputs) {
        this.interpreter = interpreter;
        this.validator = validator;
        this.workflows = workflows;
        this.revisions = revisions;
        this.analyses = analyses;
        this.items = items;
        this.questions = questions;
        this.outputs = outputs;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processAfterCommit(RequirementSubmittedEvent event) {
        process(event);
    }

    public void process(RequirementSubmittedEvent event) {
        var workflow = workflows.findById(event.workflowId()).orElseThrow();
        var revision = revisions.findById(event.revisionId()).orElseThrow();
        if (analyses.findByRevisionId(revision.getId()).isPresent()) return;

        RequirementAnalysis analysis = validator.validate(interpreter.interpret(event.requirement(), event.repositoryPath()));
        Instant now = Instant.now();
        RequirementAnalysisEntity entity = new RequirementAnalysisEntity(UUID.randomUUID(), revision.getId(),
                analysis.normalizedProblem(), analysis.ambiguity().clarificationRequired(),
                analysis.ambiguity().riskLevel(), !analysis.ambiguity().clarificationRequired(), now);
        analyses.save(entity);
        items.saveAll(toItems(entity.getId(), analysis));
        questions.saveAll(analysis.ambiguity().questions().stream()
                .map(question -> new ClarificationQuestionEntity(UUID.randomUUID(), entity.getId(), question.id(),
                        question.dimension(), question.prompt()))
                .toList());
        activateOrCreateOutputs(revision.getId());

        WorkflowStatus next = analysis.ambiguity().clarificationRequired()
                ? WorkflowStatus.AWAITING_CLARIFICATION : WorkflowStatus.PLANNING;
        revision.transition(next);
        workflow.transition(next, now);
        revisions.save(revision);
        workflows.save(workflow);
    }

    private List<RequirementItemEntity> toItems(UUID analysisId, RequirementAnalysis analysis) {
        List<RequirementItemEntity> result = new ArrayList<>();
        analysis.acceptanceCriteria().forEach(item -> result.add(new RequirementItemEntity(UUID.randomUUID(), analysisId,
                ItemType.ACCEPTANCE_CRITERION, item.id(), item.description(), item.behavioral())));
        append(result, analysisId, ItemType.ASSUMPTION, "AS", analysis.assumptions());
        append(result, analysisId, ItemType.CONSTRAINT, "CO", analysis.constraints());
        append(result, analysisId, ItemType.RISK, "RI", analysis.risks());
        return result;
    }

    private void append(List<RequirementItemEntity> target, UUID analysisId, ItemType type,
                        String prefix, List<String> values) {
        for (int index = 0; index < values.size(); index++) {
            target.add(new RequirementItemEntity(UUID.randomUUID(), analysisId, type,
                    prefix + "-" + (index + 1), values.get(index), false));
        }
    }

    private void activateOrCreateOutputs(UUID revisionId) {
        List<RevisionOutputEntity> existing = outputs.findByRevisionIdOrderByOutputKey(revisionId);
        if (existing.isEmpty()) {
            outputs.saveAll(List.of(
                    new RevisionOutputEntity(UUID.randomUUID(), revisionId, "normalized-requirement",
                            InputDimension.REQUIREMENT, OutputStatus.ACTIVE, null),
                    new RevisionOutputEntity(UUID.randomUUID(), revisionId, "ambiguity-analysis",
                            InputDimension.REQUIREMENT, OutputStatus.ACTIVE, null),
                    new RevisionOutputEntity(UUID.randomUUID(), revisionId, "repository-selection",
                            InputDimension.REPOSITORY, OutputStatus.ACTIVE, null)));
            return;
        }
        existing.stream().filter(output -> output.getInputDimension() == InputDimension.REQUIREMENT
                        && output.getStatus() == OutputStatus.PENDING)
                .forEach(RevisionOutputEntity::activate);
        outputs.saveAll(existing);
    }
}
