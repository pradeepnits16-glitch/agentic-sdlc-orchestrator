package com.pradeep.agenticsdlcorchestrator.workflow.api;

import com.pradeep.agenticsdlcorchestrator.agent.SpecialistAgentOrchestrator.AgentInvocationSummary;
import com.pradeep.agenticsdlcorchestrator.planning.domain.PlanModels.EngineeringTaskPlan;
import com.pradeep.agenticsdlcorchestrator.repository.domain.RepositoryModels.RepositoryMap;
import com.pradeep.agenticsdlcorchestrator.workflow.domain.WorkflowStatus;
import java.util.UUID;
import java.util.List;

public record PlanningResponse(UUID workflowId, UUID revisionId, WorkflowStatus status,
                               String workspaceLocation, String baselineManifestHash,
                               String repositoryAnalysisHash, RepositoryMap repositoryAnalysis,
                               String planHash, EngineeringTaskPlan plan,
                               List<AgentInvocationSummary> agentInvocations) {
}
