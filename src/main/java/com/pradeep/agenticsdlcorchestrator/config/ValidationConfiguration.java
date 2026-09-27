package com.pradeep.agenticsdlcorchestrator.config;

import com.pradeep.agenticsdlcorchestrator.validation.FixedMavenCapabilityTool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidationConfiguration {
    @Bean
    FixedMavenCapabilityTool fixedMavenCapabilityTool(ValidationProperties properties) {
        return new FixedMavenCapabilityTool(properties);
    }

}
