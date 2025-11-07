package com.ai.healthcare.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfig {

    @Value("${spring.ai.openai.api-key}")
    private String openAiApiKey;

    @Value("${spring.ai.openai.chat.option.model}")
    private String openAiModel;
    @Value("${spring.ai.vertex.chat.option.model}")
    private String vertexAiGeminiModel;

    // Chat client using OpenAI
    @Bean
    public ChatClient openAiChatClient(OpenAiChatModel openAiChatModel) {
        return ChatClient.builder(openAiChatModel)
                .defaultOptions(ChatOptions.builder()
                        .model(openAiModel)
                    //    .temperature(0.5)
                      //  .maxTokens(200)
                        .build()).build();
    }

    @Bean
    public ChatClient geminiChatClient(VertexAiGeminiChatModel vertexAiGeminiChatModel) {
        return ChatClient.builder(vertexAiGeminiChatModel)
                .defaultOptions(ChatOptions.builder()
                        .model(vertexAiGeminiModel)
      //                  .temperature(0.5)
      //                  .maxTokens(200)
                        .build()).build();
    }
}
