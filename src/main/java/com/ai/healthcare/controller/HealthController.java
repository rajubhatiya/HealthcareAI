package com.ai.healthcare.controller;

import com.ai.healthcare.model.DietPlanResponse;
import com.ai.healthcare.model.MultiModelMediaResponse;
import com.ai.healthcare.model.TextToAudioRequest;
import com.ai.healthcare.service.AIOrchestrator;
import com.ai.healthcare.service.AudioMediaService;
import com.ai.healthcare.service.MultiModelAIService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    private final AudioMediaService audioMediaService;

    public HealthController(AIOrchestrator aiOrchestrator,
                            MultiModelAIService multiModelAIService,
                            AudioMediaService audioMediaService) {
        this.aiOrchestrator = aiOrchestrator;
        this.multiModelAIService = multiModelAIService;
        this.audioMediaService = audioMediaService;
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

    /**
     * Converts input text to audio. Placeholder until audio provider is wired.
     *
     * @param text The text to synthesize into audio.
     * @return A response with a not implemented message.
     */
    @PostMapping(value = "/textToAudio",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = "audio/mpeg")
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

    /**
     * Converts an audio file to text. Placeholder until audio provider is wired.
     *
     * @param audio The audio file to transcribe.
     * @return A response with a not implemented message.
     */
    @PostMapping(value = "/audioToText",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> audioToText(@RequestParam("audio") MultipartFile audio) {
        if (audio == null || audio.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("audio file is required.");
        }
        log.info("Received request for audio-to-text: {}", audio.getOriginalFilename());
        String transcript = audioMediaService.audioToText(audio);
        return ResponseEntity.ok(transcript);
    }
}
