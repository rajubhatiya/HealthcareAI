package com.ai.platform.retrieval.utility;

import com.ai.platform.retrieval.model.DietPlanResponse;
import org.springframework.stereotype.Component;

@Component
public class AIResponseValidator {
    public void validateDiet(DietPlanResponse response) {
        if (response == null) {
            throw new RuntimeException("Generated diet plan is null");
        }
        if (response.getTitle() == null || response.getTitle().isEmpty()) {
            response.setTitle("Generic Diet Plan");
        }
        // Add more logic if needed
    }
}
