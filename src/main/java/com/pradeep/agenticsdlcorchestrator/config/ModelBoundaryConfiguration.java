package com.pradeep.agenticsdlcorchestrator.config;

import com.pradeep.agenticsdlcorchestrator.execution.ModelProvider;
import com.pradeep.agenticsdlcorchestrator.model.BoundedModelGateway;
import com.pradeep.agenticsdlcorchestrator.model.DeterministicModelProvider;
import com.pradeep.agenticsdlcorchestrator.model.JdkOpenAiTransport;
import com.pradeep.agenticsdlcorchestrator.model.OpenAiResponsesModelProvider;
import com.pradeep.agenticsdlcorchestrator.patch.FileOperationProposalAgent;
import com.pradeep.agenticsdlcorchestrator.patch.ModelFileOperationProposalAgent;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;
import com.pradeep.agenticsdlcorchestrator.observability.PlatformMetrics;

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
                                            ObjectMapper objectMapper, PlatformMetrics metrics) {
        return new BoundedModelGateway(provider, properties, objectMapper, metrics);
    }

    @Bean
    FileOperationProposalAgent fileOperationProposalAgent(BoundedModelGateway gateway,
                                                          ModelProviderProperties properties,
                                                          ObjectMapper objectMapper) {
        return new ModelFileOperationProposalAgent(gateway, properties, objectMapper);
    }
}
