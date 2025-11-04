package com.ai.healthcare.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HealthAdvisor {
    private final ChatClient chatClient;
    @Value("${OPENAI_API_KEY:NOT_FOUND}")
    private String openAiApiKey;
    public HealthAdvisor(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String getHealthTip(String userPrompt) {
        /*
        String systemPrompt = """
            You are a helpful healthcare assistant. 
            Provide health tips and wellness advice in a friendly, easy-to-understand tone.
            if someone ask anything which is not related to health, please say that you only provide health tips only.
            Always include this disclaimer:
            "This is not medical advice. Please consult a healthcare professional."
            """;

        // Use ChatClient fluent API
        return chatClient.prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .options(ChatOptions.builder()
                        .model("gpt-4o-mini") // or use model configured in ChatModel bean
                        .temperature(0.5)
                        .maxTokens(200)
                        .build())
                .call()
                .content(); // Returns text content of the response


         */
        System.out.println("✅ OPENAI_API_KEY = " + openAiApiKey);
        return "Hello";
    }
}
