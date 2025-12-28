package com.ai.healthcare.controller;

import com.ai.healthcare.model.HealthResponse;
import com.ai.healthcare.model.UserPrompt;
import com.ai.healthcare.service.AnagramsService;
import com.ai.healthcare.service.HealthAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HealthTipsController {

    private static final String[] ANAGRAM_INPUT = {"cat", "god", "act", "dog", "tac", "abb", "aac"};

    private final HealthAdvisor healthAdvisor;
    private final AnagramsService anagramsService;

    @Autowired
    public HealthTipsController(HealthAdvisor healthAdvisor, AnagramsService anagramsService) {
        this.healthAdvisor = healthAdvisor;
        this.anagramsService = anagramsService;
    }

    @PostMapping("/ask")
    public HealthResponse askAI(@RequestBody UserPrompt prompt) {
        return new HealthResponse(healthAdvisor.getHealthTip(prompt.getMessage()));
    }

    @PostMapping("/askWithTokenUsage")
    public HealthResponse askAIWithTokenUsage(@RequestBody UserPrompt prompt) {
        return new HealthResponse(healthAdvisor.getHealthTipWithTokenUsage(prompt.getMessage()));
    }

    @GetMapping("/anagrams")
    public List<List<String>> getAnagrams() {
        // Returns the list directly so Spring handles JSON serialization
        return anagramsService.groupAnagrams(ANAGRAM_INPUT);
    }
    
}
