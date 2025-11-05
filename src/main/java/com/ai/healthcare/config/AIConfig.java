package com.ai.healthcare.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.model.ApiKey;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

@Configuration
public class AIConfig {

    @Value("${spring.ai.openai.api-key}")
    private String openAiApiKey;

    @Value("${spring.ai.openai.chat.option.model}")
    private String openAiModel;

    /**
     * Define the ChatModel bean (OpenAI GPT model)
     */
    @Bean
    public ChatModel chatModel(RestClient.Builder restClientBuilder, WebClient.Builder webClientBuilder) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();

        // Simple no-op error handler (optional)
        ResponseErrorHandler errorHandler = new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return false;
            }
        };

        // Create OpenAI API instance
        OpenAiApi openAiApi = new OpenAiApi(
                "https://api.openai.com/v1",
                new ApiKey() {
                    @Override
                    public String getValue() {
                        return openAiApiKey;
                    }
                },
                headers,
                "/chat/completions",
                "/embeddings",
                restClientBuilder,
                webClientBuilder,
                errorHandler
        );

        // Default model configuration
        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .model(openAiModel)
                .temperature(0.7)
                .build();

        // Return a fully configured ChatModel
        return OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .defaultOptions(options)
                .build();
    }

    /**
     * Define ChatClient bean using ChatModel
     */
    @Bean
    public ChatClient chatClient(ChatModel chatModel) {
        return ChatClient.builder(chatModel).build();
    }
}
