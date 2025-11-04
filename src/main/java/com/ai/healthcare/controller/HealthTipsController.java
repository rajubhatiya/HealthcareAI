package com.ai.healthcare.controller;

import com.ai.healthcare.model.HealthResponse;
import com.ai.healthcare.model.UserPrompt;
import com.ai.healthcare.service.HealthAdvisor;
import com.ai.healthcare.service.HealthTipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthTipsController {

    @Autowired
    HealthAdvisor healthAdvisor;

    @PostMapping("/ask")
    public HealthResponse askAI(@RequestBody UserPrompt prompt) {
        String reply = healthAdvisor.getHealthTip(prompt.getMessage());
        return new HealthResponse(reply);
    }
}
