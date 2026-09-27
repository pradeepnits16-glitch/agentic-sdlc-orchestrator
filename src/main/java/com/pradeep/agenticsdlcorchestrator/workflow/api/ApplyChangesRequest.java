package com.pradeep.agenticsdlcorchestrator.workflow.api;

import jakarta.validation.constraints.NotBlank;

public record ApplyChangesRequest(@NotBlank String planHash) {}
