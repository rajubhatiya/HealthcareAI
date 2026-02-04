package com.ai.healthcare.service;

import com.ai.healthcare.model.DietPlanResponse;
import com.ai.healthcare.utility.AIResponseValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Orchestrator service to coordinate interactions between various AI
 * components.
 * This class delegates requests to specific services (PromptService,
 * OpenAIClient) and validates responses.
 */
@Service
public class AIOrchestrator {

        private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AIOrchestrator.class);

        private final PromptService promptService;
        private final OpenAIClient openAIClient;
        private final AIResponseValidator validator;

        @Autowired
        public AIOrchestrator(PromptService promptService, OpenAIClient openAIClient, AIResponseValidator validator) {
                this.promptService = promptService;
                this.openAIClient = openAIClient;
                this.validator = validator;
        }

        /**
         * Generates a vegetarian diet plan using OpenAI.
         *
         * @param userRequest The user's specific diet request.
         * @return A valid DietPlanResponse.
         */
        public DietPlanResponse generateVegetarianDiet(
                        String userRequest) {

                log.info("Starting generation of vegetarian diet plan for request: {}", userRequest);

                String systemPrompt = promptService.getVegetarianSystemPrompt();

                String userPrompt = promptService.buildDietUserPrompt(userRequest);

                DietPlanResponse response = openAIClient.call(systemPrompt,
                                userPrompt,
                                DietPlanResponse.class);

                log.debug("Received raw response from OpenAI. Validating...");

                validator.validateDiet(response);

                log.info("Successfully generated and validated diet plan.");

                return response;
        }
}
