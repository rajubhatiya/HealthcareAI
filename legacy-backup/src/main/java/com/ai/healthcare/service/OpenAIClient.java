package com.ai.healthcare.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class OpenAIClient {

    @Qualifier("openAiChatClient")
    private final ChatClient openAi;

    public <T> T call(
            String system,
            String user,
            Class<T> responseType) {
        return openAi.prompt()
                .system(system)
                .user(user)
                .call()
                .entity(responseType); // maps JSON → DTO
    }

    public OpenAIClient(@Qualifier("openAiChatClient") ChatClient openAi) {
        this.openAi = openAi;
    }
}
