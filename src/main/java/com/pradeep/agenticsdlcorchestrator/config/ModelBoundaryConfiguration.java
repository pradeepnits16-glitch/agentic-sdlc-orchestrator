package com.pradeep.agenticsdlcorchestrator.config;

import com.pradeep.agenticsdlcorchestrator.execution.ModelProvider;
import com.pradeep.agenticsdlcorchestrator.model.BoundedModelGateway;
import com.pradeep.agenticsdlcorchestrator.model.DeterministicModelProvider;
import com.pradeep.agenticsdlcorchestrator.model.JdkOpenAiTransport;
import com.pradeep.agenticsdlcorchestrator.model.OpenAiResponsesModelProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class ModelBoundaryConfiguration {
    @Bean
    @ConditionalOnProperty(name = "agentic.model.provider", havingValue = "deterministic", matchIfMissing = true)
    ModelProvider deterministicModelProvider(ObjectMapper objectMapper) {
        return new DeterministicModelProvider(objectMapper);
    }

    @Bean
    @ConditionalOnProperty(name = "agentic.model.provider", havingValue = "openai")
    ModelProvider openAiModelProvider(ModelProviderProperties properties, ObjectMapper objectMapper) {
        return new OpenAiResponsesModelProvider(properties, new JdkOpenAiTransport(), objectMapper);
    }

    @Bean
    BoundedModelGateway boundedModelGateway(ModelProvider provider, ModelProviderProperties properties,
                                            ObjectMapper objectMapper) {
        return new BoundedModelGateway(provider, properties, objectMapper);
    }
}
