package com.ai.platform.retrieval.service;

import org.springframework.stereotype.Service;

@Service
public class PromptService {

    public String getVegetarianSystemPrompt() {
        return "You are a specialized nutritionist assistant. Generate 100% vegetarian diet plans. " +
                "Format the response as specific JSON matching the requested structure.";
    }

    public String buildDietUserPrompt(String request) {
        return "Create a detailed vegetarian diet plan for: " + request;
    }
}
