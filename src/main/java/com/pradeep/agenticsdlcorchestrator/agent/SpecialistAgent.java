package com.pradeep.agenticsdlcorchestrator.agent;

import com.pradeep.agenticsdlcorchestrator.agent.SpecialistAgentModels.SpecialistAgentInput;
import com.pradeep.agenticsdlcorchestrator.agent.SpecialistAgentModels.SpecialistAgentResult;

public interface SpecialistAgent {
    SpecialistAgentRole role();
    SpecialistAgentResult execute(SpecialistAgentInput input);
}
