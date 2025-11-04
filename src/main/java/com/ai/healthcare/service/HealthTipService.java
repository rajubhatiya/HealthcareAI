package com.ai.healthcare.service;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import jakarta.annotation.PostConstruct;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class HealthTipService {
    private final RestClient restClient;
    ChatClient chatClient;

    @Value("${OPENAI_API_KEY:NOT_FOUND}")
    private String openAiApiKey;



    @Value("${openai.api.url}")
    private String apiUrl;

    public HealthTipService() {
        this.restClient = RestClient.create();
    }

    @PostConstruct
    public void printKey() {
        System.out.println("✅ OPENAI_API_KEY = " + openAiApiKey);
    }
    public String getHealthTip(String userPrompt) {
/*
        String systemPrompt = """
                    You are a helpful healthcare assistant. 
                    Provide health tips and wellness advice in a friendly, easy-to-understand tone.
                    Always include this disclaimer:
                    "This is not medical advice. Please consult a healthcare professional."
                """;

        Map<String, Object> request = Map.of(
                "model", "gpt-4o",
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userPrompt)
                ),
                "max_tokens", 150 // limit response length
        );

        Map<String, Object> response = restClient.post()
                .uri(apiUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + openAiApiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(Map.class);

        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");

        return (String) message.get("content");
  */
        return "Hello";
    }

}
