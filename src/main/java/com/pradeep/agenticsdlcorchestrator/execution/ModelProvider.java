package com.pradeep.agenticsdlcorchestrator.execution;

import com.pradeep.agenticsdlcorchestrator.execution.model.ExecutionModels.ModelRequest;
import com.pradeep.agenticsdlcorchestrator.execution.model.ExecutionModels.ModelResponse;

public interface ModelProvider {
    ModelResponse generate(ModelRequest request);
}

