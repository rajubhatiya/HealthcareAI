package com.ai.platform.retrieval.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class OpenAIClient {

    private final ChatClient openAi;

    public OpenAIClient(ChatClient.Builder builder) {
        this.openAi = builder.build();
    }

    public <T> T call(String system, String user, Class<T> responseType) {
        return openAi.prompt()
                .system(system)
                .user(user)
                .call()
                .entity(responseType);
    }
}
