package com.pradeep.agenticsdlcorchestrator.requirement.agent;

import com.pradeep.agenticsdlcorchestrator.requirement.domain.RequirementAnalysis;

public interface RequirementInterpreterAgent {
    RequirementAnalysis interpret(String requirement, String repositoryPath);
}

