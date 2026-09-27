package com.pradeep.agenticsdlcorchestrator;

import com.pradeep.agenticsdlcorchestrator.config.AgenticExecutionProperties;
import com.pradeep.agenticsdlcorchestrator.config.ModelProviderProperties;
import com.pradeep.agenticsdlcorchestrator.config.RepositoryToolProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableConfigurationProperties({AgenticExecutionProperties.class, RepositoryToolProperties.class,
        ModelProviderProperties.class})
@EnableAsync
public class AgenticSdlcOrchestrator {
    public static void main(String[] args) {
        SpringApplication.run(AgenticSdlcOrchestrator.class, args);
    }
}
