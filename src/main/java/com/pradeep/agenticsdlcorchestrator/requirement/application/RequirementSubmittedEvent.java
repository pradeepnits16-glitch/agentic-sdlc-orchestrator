package com.pradeep.agenticsdlcorchestrator.requirement.application;

import java.util.UUID;

public record RequirementSubmittedEvent(UUID workflowId, UUID revisionId, String requirement, String repositoryPath) {
}

