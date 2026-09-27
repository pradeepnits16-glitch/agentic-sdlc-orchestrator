package com.pradeep.agenticsdlcorchestrator.execution;

import com.pradeep.agenticsdlcorchestrator.execution.model.ExecutionModels.AgentExecutionResult;
import com.pradeep.agenticsdlcorchestrator.execution.model.ExecutionModels.ExecutionContext;
import com.pradeep.agenticsdlcorchestrator.workflow.domain.AgentTask;

public interface AgentExecutor {
    AgentExecutionResult execute(AgentTask task, ExecutionContext context);
}

