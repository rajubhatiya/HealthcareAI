package com.ai.healthcare.service;

import org.springframework.stereotype.Service;

@Service
public class PromptService {

    public String getVegetarianSystemPrompt() {
        return """
        You are a healthcare assistant specializing in vegetarian nutrition.

        Rules:
        - Return ONLY valid JSON
        - Do NOT include markdown
        - Do NOT diagnose
        - Provide balanced meals

        Always include:
        "This is not medical advice. Please consult a healthcare professional."
        """;
    }

    public String buildDietUserPrompt(String message) {

        return """
        Create a 1-day vegetarian diet plan.

        User request:
        """ + message;
    }
}
