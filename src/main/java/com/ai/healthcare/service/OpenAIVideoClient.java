package com.ai.healthcare.service;

import com.ai.healthcare.model.VideoGenerationRequest;
import com.ai.healthcare.model.VideoJobResponse;
import com.ai.healthcare.model.VideoProperties;
import com.ai.healthcare.model.VideoStatusResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

/**
 * Client for interacting with OpenAI's video generation API.
 * This class handles the submission of video generation requests and polling
 * for the results.
 * It is robustly designed to handle network errors and API limitations.
 * 
 * Note: The endpoint `/videos/generations` is experimental and may not be
 * publicly available.
 */
@Service
public class OpenAIVideoClient {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(OpenAIVideoClient.class);

    private final RestClient restClient;
    private final VideoProperties properties;

    public OpenAIVideoClient(RestClient restClient, VideoProperties properties) {
        this.restClient = restClient;
        this.properties = properties;
    }

    /**
     * Submits a request to generate a video based on the provided prompt and polls
     * for the result.
     *
     * @param prompt The text prompt describing the desired video.
     * @return The URL of the generated video.
     * @throws RuntimeException If the generation fails, times out, or the API
     *                          returns an error.
     */
    public String generateVideo(String prompt) {

        VideoGenerationRequest request = VideoGenerationRequest.builder()
                .model(properties.getModel())
                .prompt(prompt)
                .duration(properties.getDuration())
                .build();

        try {

            log.info("Submitting video generation request. prompt='{}'", prompt);

            VideoJobResponse response = restClient.post()
                    .uri("/videos/generations")
                    .body(request)
                    .retrieve()
                    .body(VideoJobResponse.class);

            if (response == null || response.getId() == null) {
                log.error("Received null response or ID from OpenAI video endpoint");
                throw new IllegalStateException("Invalid OpenAI response: ID is missing");
            }

            log.info("Video job submitted successfully. jobId={}", response.getId());

            return pollForVideo(response.getId());

        } catch (org.springframework.web.client.HttpClientErrorException
                | org.springframework.web.client.HttpServerErrorException ex) {
            log.error("Video generation API failed. Status: {}, Response: {}", ex.getStatusCode(),
                    ex.getResponseBodyAsString());
            throw new RuntimeException("OpenAI video generation API error: " + ex.getStatusCode(), ex);
        } catch (Exception ex) {
            log.error("Video generation failed unexpectedly", ex);
            throw new RuntimeException("OpenAI video generation failed", ex);
        }
    }

    /**
     * Polls the video generation status until completion, failure, or timeout.
     *
     * @param jobId The ID of the submitted video generation job.
     * @return The URL of the generated video.
     * @throws RuntimeException If the job fails, expires, or polling times out.
     */
    private String pollForVideo(String jobId) {

        log.info("Starting polling for video jobId={}", jobId);

        for (int i = 0; i < properties.getPollAttempts(); i++) {

            VideoStatusResponse statusResponse = null;
            try {
                statusResponse = restClient.get()
                        .uri("/videos/generations/{id}", jobId)
                        .retrieve()
                        .body(VideoStatusResponse.class);
            } catch (Exception e) {
                log.warn("Failed to poll video status for jobId={}. Attempt {}/{}. Error: {}", jobId, i + 1,
                        properties.getPollAttempts(), e.getMessage());
                sleepSafely();
                continue;
            }

            if (statusResponse == null) {
                log.warn("Received null status response for jobId={}", jobId);
                sleepSafely();
                continue;
            }

            String status = statusResponse.getStatus();

            if ("completed".equalsIgnoreCase(status)) {
                if (statusResponse.getOutput() != null && statusResponse.getOutput().getUrl() != null) {
                    String url = statusResponse.getOutput().getUrl();
                    log.info("Video generation completed. jobId={}", jobId);
                    return url;
                } else {
                    log.error("Video completed but output URL is missing. jobId={}", jobId);
                    throw new IllegalStateException("Video completed but URL is missing");
                }
            } else if ("failed".equalsIgnoreCase(status) || "expired".equalsIgnoreCase(status)) {
                log.error("Video generation failed or expired. jobId={}, status={}", jobId, status);
                throw new RuntimeException("Video generation failed with status: " + status);
            }

            // Log progress every 5 attempts to avoid log spam
            if (i % 5 == 0) {
                log.info("Polling video status... jobId={}, status={}, attempt={}/{}", jobId, status, i + 1,
                        properties.getPollAttempts());
            }

            sleepSafely();
        }

        log.error("Timout waiting for video generation. jobId={}", jobId);
        throw new RuntimeException("Video generation timeout for jobId=" + jobId);
    }

    private void sleepSafely() {

        try {
            Thread.sleep(properties.getPollDelayMs());
        } catch (InterruptedException ex) {

            Thread.currentThread().interrupt(); // VERY IMPORTANT
            throw new RuntimeException("Thread interrupted", ex);
        }
    }
}
