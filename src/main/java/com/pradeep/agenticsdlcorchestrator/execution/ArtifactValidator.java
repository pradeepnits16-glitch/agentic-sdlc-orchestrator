package com.pradeep.agenticsdlcorchestrator.execution;

import com.pradeep.agenticsdlcorchestrator.artifact.ArtifactModels.EngineeringArtifact;
import com.pradeep.agenticsdlcorchestrator.artifact.ArtifactModels.ValidationResult;
import com.pradeep.agenticsdlcorchestrator.execution.model.ExecutionModels.ValidationContext;

public interface ArtifactValidator {
    ValidationResult validate(EngineeringArtifact artifact, ValidationContext context);
}

