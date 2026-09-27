package com.pradeep.agenticsdlcorchestrator.workflow.api;

import com.pradeep.agenticsdlcorchestrator.workflow.domain.WorkflowStatus;
import java.util.UUID;

public record WorkflowSubmissionResponse(UUID workflowId, UUID revisionId, int revision,
                                         WorkflowStatus status, String requirementHash) {
}

