package com.ai.healthcare.service;

import org.springframework.stereotype.Service;

/**
 * Service responsible for managing and providing system and user prompts.
 * Centralizes prompt logic to ensure consistency across the application.
 */
@Service
public class PromptService {

    /**
     * Retrieves the standard system prompt for the vegetarian nutrition assistant.
     * Includes rules for JSON output and medical disclaimers.
     *
     * @return The system prompt string.
     */
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

    /**
     * Builds a user prompt for a specific diet request.
     *
     * @param message The user's specific requirements (e.g., "high protein",
     *                "gluten-free").
     * @return The formatted user prompt string.
     */
    public String buildDietUserPrompt(String message) {

        return """
                Create a 1-day vegetarian diet plan.

                User request:
                """ + message;
    }
}
