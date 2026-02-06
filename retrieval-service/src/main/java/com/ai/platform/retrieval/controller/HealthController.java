package com.ai.platform.retrieval.controller;

import com.ai.platform.retrieval.model.DietPlanResponse;
import com.ai.platform.retrieval.model.MultiModelMediaResponse;
import com.ai.platform.retrieval.model.TextToAudioRequest;
import com.ai.platform.retrieval.service.AIOrchestrator;
import com.ai.platform.retrieval.service.AudioMediaService;
import com.ai.platform.retrieval.service.MultiModelAIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/health")
public class HealthController {

    private static final Logger log = LoggerFactory.getLogger(HealthController.class);

    private final AIOrchestrator aiOrchestrator;
    private final MultiModelAIService multiModelAIService;
    private final AudioMediaService audioMediaService;

    public HealthController(AIOrchestrator aiOrchestrator,
            MultiModelAIService multiModelAIService,
            AudioMediaService audioMediaService) {
        this.aiOrchestrator = aiOrchestrator;
        this.multiModelAIService = multiModelAIService;
        this.audioMediaService = audioMediaService;
    }

    @GetMapping("/vegetarian-diet/{request}")
    public ResponseEntity<DietPlanResponse> generateDiet(@PathVariable String request) {
        log.info("Received request for vegetarian diet plan: {}", request);
        return ResponseEntity.ok(aiOrchestrator.generateVegetarianDiet(request));
    }

    @GetMapping("/multimodel-media/image/{prompt}")
    public ResponseEntity<MultiModelMediaResponse> generateImage(@PathVariable String prompt) {
        log.info("Received request for image generation: {}", prompt);
        return ResponseEntity.ok(multiModelAIService.generateImage(prompt));
    }

    @PostMapping("/multimodel-media/video/{prompt}")
    public ResponseEntity<MultiModelMediaResponse> generateVideo(@PathVariable String prompt) {
        log.info("Received request for video generation: {}", prompt);
        return ResponseEntity.ok(multiModelAIService.generateVideo(prompt));
    }

    @PostMapping(value = "/textToAudio", consumes = MediaType.APPLICATION_JSON_VALUE, produces = "audio/mpeg")
    public ResponseEntity<byte[]> textToAudio(@RequestBody TextToAudioRequest request) {
        if (request == null || request.getText() == null || request.getText().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        log.info("Received request for text-to-audio.");
        byte[] audioBytes = audioMediaService.textToAudio(request.getText());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .body(audioBytes);
    }

    @PostMapping(value = "/audioToText", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> audioToText(@RequestParam("audio") MultipartFile audio) {
        if (audio == null || audio.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("audio file is required.");
        }
        log.info("Received request for audio-to-text: {}", audio.getOriginalFilename());
        String transcript = audioMediaService.audioToText(audio);
        return ResponseEntity.ok(transcript);
    }
}
