package com.pradeep.agenticsdlcorchestrator.workflow.api;

import com.pradeep.agenticsdlcorchestrator.requirement.application.RequirementSubmittedEvent;
import com.pradeep.agenticsdlcorchestrator.workflow.domain.WorkflowStatus;
import com.pradeep.agenticsdlcorchestrator.workflow.persistence.WorkflowEntity;
import com.pradeep.agenticsdlcorchestrator.workflow.persistence.WorkflowRepository;
import com.pradeep.agenticsdlcorchestrator.workflow.persistence.WorkflowRevisionEntity;
import com.pradeep.agenticsdlcorchestrator.workflow.persistence.WorkflowRevisionRepository;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;
import java.util.UUID;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class WorkflowSubmissionService {
    private final WorkflowRepository workflows;
    private final WorkflowRevisionRepository revisions;
    private final ApplicationEventPublisher events;

    WorkflowSubmissionService(WorkflowRepository workflows, WorkflowRevisionRepository revisions,
                              ApplicationEventPublisher events) {
        this.workflows = workflows;
        this.revisions = revisions;
        this.events = events;
    }

    @Transactional
    WorkflowSubmissionResponse submit(CreateWorkflowRequest request) {
        UUID workflowId = UUID.randomUUID();
        UUID revisionId = UUID.randomUUID();
        Instant now = Instant.now();
        String requirementHash = sha256(request.requirement());
        workflows.save(new WorkflowEntity(workflowId, request.requirement(), request.repositoryPath(), now));
        revisions.save(new WorkflowRevisionEntity(revisionId, workflowId, 1, null, requirementHash, now));
        events.publishEvent(new RequirementSubmittedEvent(workflowId, revisionId,
                request.requirement(), request.repositoryPath()));
        return new WorkflowSubmissionResponse(workflowId, revisionId, 1, WorkflowStatus.RECEIVED,
                requirementHash);
    }

    private String sha256(String value) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
                    .digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is unavailable", exception);
        }
    }
}
