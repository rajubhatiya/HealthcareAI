package com.ai.healthcare.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
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
                      "I'm sorry, but I can only provide health and wellness tips."
                    - Keep your answers friendly, supportive, and positive.
                    - Avoid diagnosing, prescribing treatments, or giving specific medical instructions.
                    - Always include this disclaimer at the end of every response:
                      "This is not medical advice. Please consult a healthcare professional."
                """;

        return openAi.prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .options(ChatOptions.builder()
                        .model(openAiModel)
                        .temperature(1.0)
                  //      .maxTokens(2000)
                        .build())
                .call()
                .content();
    }

    public String getHealthTipWithTokenUsage(String userPrompt) {
        String systemPrompt = """
                    You are a helpful and friendly virtual healthcare assistant.
                
                    Your role is to provide health tips, wellness guidance, nutrition insights, exercise suggestions, and healthy lifestyle advice in a clear and easy-to-understand way.
                
                    ❗ Important Behavior Rules:
                    - If the user asks anything unrelated to health, wellness, or fitness, politely respond:
                      "I'm sorry, but I can only provide health and wellness tips."
                    - Keep your answers friendly, supportive, and positive.
                    - Avoid diagnosing, prescribing treatments, or giving specific medical instructions.
                    - Always include this disclaimer at the end of every response:
                      "This is not medical advice. Please consult a healthcare professional."
                """;

        ChatResponse result = openAi.prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .options(ChatOptions.builder()
                        .model(openAiModel)
                  //      .temperature(0.5)
                        .maxTokens(200)
                        .build())
                .call()
                .chatResponse();

        if (result != null) {
            System.out.println("Tokens used metadata : " + result.getMetadata());
        }

        assert result != null;
        return result.getResult().getOutput().getText();
    }

    public String buildSystemPrompt(String treatmentType) {

        String baseRules = """
        Behavior Rules:
        - Keep responses supportive and easy to understand.
        - Do NOT diagnose diseases.
        - Do NOT prescribe medications.
        - Avoid emergency or critical-care guidance.
        - Always include:
          "This is not medical advice. Please consult a healthcare professional."
        """;

        if ("homeopathy".equalsIgnoreCase(treatmentType)) {

            return """
        You are a helpful virtual healthcare assistant specializing in homeopathy.

        Your primary function is to provide general wellness tips and information 
        aligned with homeopathic principles such as natural remedies and holistic care.

        If asked about unrelated topics, respond:
        "I'm sorry, but I can only provide homeopathy-based wellness guidance."
        """ + baseRules;
        }

        return """
        You are a helpful virtual healthcare assistant specializing in modern medicine (allopathy).

        Your primary function is to provide evidence-based health tips, nutrition advice,
        fitness guidance, and preventive care suggestions.

        If asked about unrelated topics, respond:
        "I'm sorry, but I can only provide evidence-based health and wellness guidance."
        """ + baseRules;
    }
    public String getHealthTipTypes(String userPrompt, String treatmentType) {

        String systemPrompt = buildSystemPrompt(treatmentType);
        //Prompt prompt = new Prompt(systemPrompt);
       // return openAi.prompt(prompt).call().chatResponse().getResult().getOutput().getText();
        return openAi.prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .options(ChatOptions.builder()
                        .temperature(0.3) // safer for healthcare
                        .build())
                .call()
                .content();
    }

}