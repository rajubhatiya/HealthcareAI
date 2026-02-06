package com.ai.healthcare.utility;

import com.ai.healthcare.exception.AIResponseException;
import com.ai.healthcare.model.DietPlanResponse;
import org.springframework.stereotype.Component;

@Component
public class AIResponseValidator {

    public void validateDiet(DietPlanResponse response){

        if(response.getBreakfast() == null ||
           response.getLunch() == null ||
           response.getDinner() == null){

            throw new AIResponseException(
                    "Invalid AI diet response");
        }
    }
}
