package com.ai.healthcare.controller;

import com.ai.healthcare.model.DietPlanResponse;
import com.ai.healthcare.model.MultiModelMediaResponse;
import com.ai.healthcare.service.AIOrchestrator;
import com.ai.healthcare.service.MultiModelAIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for health-related AI operations.
 * Exposes endpoints for diet plans and multi-modal media generation (images,
 * videos).
 */
@RestController
@RequestMapping("/api/v1/health")
public class HealthController {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(HealthController.class);

    private final AIOrchestrator aiOrchestrator;
    private final MultiModelAIService multiModelAIService;

    public HealthController(AIOrchestrator aiOrchestrator, MultiModelAIService multiModelAIService) {
        this.aiOrchestrator = aiOrchestrator;
        this.multiModelAIService = multiModelAIService;
    }

    /**
     * Generates a vegetarian diet plan based on the user's request.
     *
     * @param request The user's specific diet request or preferences.
     * @return A structured diet plan response.
     */
    @GetMapping("/vegetarian-diet/{request}")
    public ResponseEntity<DietPlanResponse> generateDiet(
            @PathVariable String request) {
        log.info("Received request for vegetarian diet plan: {}", request);
        return ResponseEntity.ok(
                aiOrchestrator.generateVegetarianDiet(request));
    }

    /**
     * Generates an image based on the provided prompt.
     *
     * @param prompt The description of the image to generate.
     * @return A response containing the image URL.
     */
    @GetMapping("/multimodel-media/image/{prompt}")
    public ResponseEntity<MultiModelMediaResponse> generateImage(@PathVariable String prompt) {
        log.info("Received request for image generation: {}", prompt);
        return ResponseEntity.ok(multiModelAIService.generateImage(prompt));
    }

    /**
     * Generates a video based on the provided prompt.
     *
     * @param prompt The description of the video to generate.
     * @return A response containing the video URL and status.
     */
    @GetMapping("/multimodel-media/video/{prompt}")
    public ResponseEntity<MultiModelMediaResponse> generateVideo(@PathVariable String prompt) {
        log.info("Received request for video generation: {}", prompt);
        return ResponseEntity.ok(multiModelAIService.generateVideo(prompt));
    }
}
