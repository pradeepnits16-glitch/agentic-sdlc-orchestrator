package com.pradeep.agenticsdlcorchestrator;

import com.pradeep.agenticsdlcorchestrator.config.AgenticExecutionProperties;
import com.pradeep.agenticsdlcorchestrator.config.ModelProviderProperties;
import com.pradeep.agenticsdlcorchestrator.config.PatchPolicyProperties;
import com.pradeep.agenticsdlcorchestrator.config.RepositoryToolProperties;
import com.pradeep.agenticsdlcorchestrator.config.ValidationProperties;
import com.pradeep.agenticsdlcorchestrator.config.GovernanceProperties;
import com.pradeep.agenticsdlcorchestrator.coordination.CoordinationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties({AgenticExecutionProperties.class, RepositoryToolProperties.class,
        ModelProviderProperties.class, PatchPolicyProperties.class, ValidationProperties.class,
        GovernanceProperties.class, CoordinationProperties.class})
@EnableAsync
@EnableScheduling
public class AgenticSdlcOrchestrator {
    public static void main(String[] args) {
        SpringApplication.run(AgenticSdlcOrchestrator.class, args);
    }
}
