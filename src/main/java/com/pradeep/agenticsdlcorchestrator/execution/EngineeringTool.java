package com.pradeep.agenticsdlcorchestrator.execution;

import com.pradeep.agenticsdlcorchestrator.execution.model.ExecutionModels.ToolRequest;
import com.pradeep.agenticsdlcorchestrator.execution.model.ExecutionModels.ToolResult;

public interface EngineeringTool {
    ToolResult execute(ToolRequest request);
}

