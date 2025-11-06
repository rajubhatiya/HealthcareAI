package com.ai.healthcare.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HealthAdvisor {
    @Value("${spring.ai.openai.chat.option.model}")
    private String openAiModel;

    private final ChatClient openAi;
    private final ChatClient gemini;

    public HealthAdvisor(@Qualifier("openAiChatClient") ChatClient openAi,
                         @Qualifier("geminiChatClient") ChatClient gemini) {
        this.openAi = openAi;
        this.gemini = gemini;
    }


    public String getHealthTip(String userPrompt) {

        String systemPrompt = """
                You are a helpful and friendly virtual healthcare assistant.
                
                Your role is to provide health tips, wellness guidance, nutrition insights, exercise suggestions, and healthy lifestyle advice in a clear and easy-to-understand way.
                
                ❗ Important Behavior Rules:
                - If the user asks anything unrelated to health, wellness, or fitness, politely respond:
                  "I’m sorry, but I can only provide health and wellness tips."
                - Keep your answers friendly, supportive, and positive.
                - Avoid diagnosing, prescribing treatments, or giving specific medical instructions.
                - Always include this disclaimer at the end of every response:
                  "This is not medical advice. Please consult a healthcare professional."
            """;

        // Use ChatClient fluent API
        return openAi.prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .options(ChatOptions.builder()
                        .model(openAiModel) // or use model configured in ChatModel bean
                        .temperature(0.5)
                        .maxTokens(200)
                        .build())
                .call()
                .content(); // Returns text content of the response
    }
}