package com.ai.platform.retrieval.service;

import com.ai.platform.retrieval.model.DietPlanResponse;
import com.ai.platform.retrieval.utility.AIResponseValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AIOrchestrator {

    private static final Logger log = LoggerFactory.getLogger(AIOrchestrator.class);

    private final PromptService promptService;
    private final OpenAIClient openAIClient;
    private final AIResponseValidator validator;

    @Autowired
    public AIOrchestrator(PromptService promptService, OpenAIClient openAIClient, AIResponseValidator validator) {
        this.promptService = promptService;
        this.openAIClient = openAIClient;
        this.validator = validator;
    }

    public DietPlanResponse generateVegetarianDiet(String userRequest) {
        log.info("Starting generation of vegetarian diet plan for request: {}", userRequest);

        String systemPrompt = promptService.getVegetarianSystemPrompt();
        String userPrompt = promptService.buildDietUserPrompt(userRequest);

        DietPlanResponse response = openAIClient.call(systemPrompt, userPrompt, DietPlanResponse.class);

        log.debug("Received raw response from OpenAI. Validating...");
        validator.validateDiet(response);

        log.info("Successfully generated and validated diet plan.");
        return response;
    }
}
