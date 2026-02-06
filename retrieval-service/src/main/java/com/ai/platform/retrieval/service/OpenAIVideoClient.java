package com.ai.platform.retrieval.service;

import com.ai.platform.retrieval.config.VideoProperties;
import com.ai.platform.retrieval.model.VideoGenerationRequest;
import com.ai.platform.retrieval.model.VideoJobResponse;
import com.ai.platform.retrieval.model.VideoStatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class OpenAIVideoClient {

    private static final Logger log = LoggerFactory.getLogger(OpenAIVideoClient.class);

    private final RestClient restClient;
    private final VideoProperties properties;

    public OpenAIVideoClient(RestClient.Builder builder, VideoProperties properties) {
        this.restClient = builder.baseUrl("https://api.openai.com/v1").build(); // Ensure base URL
        this.properties = properties;
    }

    public String generateVideo(String prompt) {
        VideoGenerationRequest request = VideoGenerationRequest.builder()
                .model(properties.getModel())
                .prompt(prompt)
                .duration(properties.getDuration())
                .build();

        try {
            log.info("Submitting video generation request: {}", prompt);
            // NOTE: This uses experimental OpenAI endpoints or likely a placeholder in the
            // original legacy code
            // The original legacy code assumed /videos/generations.
            // If this is not a real endpoint, it will fail (404/400) but we preserve the
            // logic.
            // For now we assume the user has access or this refers to a specific API.

            VideoJobResponse response = restClient.post()
                    .uri("/videos/generations")
                    // Auth header usually needed, assuming RestClient builder has it or we add it
                    .header("Authorization", "Bearer " + System.getenv("OPENAI_API_KEY"))
                    .body(request)
                    .retrieve()
                    .body(VideoJobResponse.class);

            if (response == null || response.getId() == null) {
                throw new IllegalStateException("Invalid OpenAI response: ID is missing");
            }
            return pollForVideo(response.getId());

        } catch (Exception e) {
            log.error("Video generation failed", e);
            throw new RuntimeException("Video generation failed", e);
        }
    }

    private String pollForVideo(String jobId) {
        log.info("Polling for video job: {}", jobId);
        for (int i = 0; i < properties.getPollAttempts(); i++) {
            try {
                Thread.sleep(properties.getPollDelayMs());
                VideoStatusResponse status = restClient.get()
                        .uri("/videos/generations/{id}", jobId)
                        .header("Authorization", "Bearer " + System.getenv("OPENAI_API_KEY"))
                        .retrieve()
                        .body(VideoStatusResponse.class);

                if (status != null && "completed".equalsIgnoreCase(status.getStatus())) {
                    return status.getOutput().getUrl();
                }
            } catch (Exception e) {
                log.warn("Polling error: {}", e.getMessage());
            }
        }
        throw new RuntimeException("Video generation timeout");
    }
}
