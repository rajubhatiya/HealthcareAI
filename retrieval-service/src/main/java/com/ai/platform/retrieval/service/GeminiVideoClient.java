package com.ai.platform.retrieval.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class GeminiVideoClient {

    private static final Logger log = LoggerFactory.getLogger(GeminiVideoClient.class);

    @Value("${gemini.video.project-id}")
    private String projectId;

    @Value("${gemini.video.location:us-central1}")
    private String location;

    @Value("${gemini.video.model:imagen-3.0-generate-001}")
    private String model;

    private final RestClient restClient;

    public GeminiVideoClient(RestClient.Builder builder) {
        this.restClient = builder.build();
    }

    public String generateVideo(String prompt) {
        log.info("Generating video with Gemini/Vertex AI. Project: {}, Model: {}", projectId, model);

        String endpoint = String.format(
                "https://%s-aiplatform.googleapis.com/v1/projects/%s/locations/%s/publishers/google/models/%s:predict",
                location, projectId, location, model);

        try {
            String token = getAccessToken();

            // Construct payload for Imagen/Gemini video generation
            // Note: Schema varies by model version. Using common Imagen video generation
            // schema.
            // {
            // "instances": [ { "prompt": "..." } ],
            // "parameters": { "sampleCount": 1, "durationSeconds": 5 }
            // }
            Map<String, Object> instance = Map.of("prompt", prompt);
            Map<String, Object> parameters = Map.of(
                    "sampleCount", 1,
                    "durationSeconds", 5);

            Map<String, Object> requestBody = Map.of(
                    "instances", List.of(instance),
                    "parameters", parameters);

            log.info("Sending request to Vertex AI: {}", endpoint);
            log.info("Request payload: {}", requestBody);

            Map response = restClient.post()
                    .uri(endpoint)
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(Map.class);

            if (response != null && response.containsKey("predictions")) {
                List<Map<String, Object>> predictions = (List<Map<String, Object>>) response.get("predictions");
                if (!predictions.isEmpty()) {
                    // Start checking for different response formats (bytesBase64, gcsUri, etc.)
                    Map<String, Object> firstPrediction = predictions.get(0);

                    // Format 1: bytesBase64 (often for smaller videos/previews)
                    if (firstPrediction.containsKey("bytesBase64")) {
                        // In a real app, upload this to S3/GCS and return URL.
                        // For this demo, we can't easily return base64 as a URL unless we use data URI,
                        // but data URIs for videos are huge.
                        // However, OpenAI interface expects a URL.
                        // Let's assume we return a truncated indicator or a data URI if small enough.
                        // Or check for gcsUri (Format 2)
                        return "data:video/mp4;base64," + firstPrediction.get("bytesBase64").toString().substring(0, 50)
                                + "...(truncated_for_log)";
                    }

                    // Format 2: struct with gcsUri or similar
                    if (firstPrediction.containsKey("gcsUri")) {
                        return firstPrediction.get("gcsUri").toString();
                    }

                    // Fallback check
                    if (firstPrediction.containsKey("video")) {
                        Object videoObj = firstPrediction.get("video");
                        if (videoObj instanceof Map) {
                            Map videoMap = (Map) videoObj;
                            if (videoMap.containsKey("gcsUri"))
                                return videoMap.get("gcsUri").toString();
                        }
                    }
                }
            }

            log.warn("No predictions found in Vertex AI response: {}", response);
            throw new RuntimeException("Vertex AI returned no video predictions.");

        } catch (Exception e) {
            log.error("Failed to generate video with Gemini", e);
            throw new RuntimeException("Gemini video generation failed: " + e.getMessage(), e);
        }
    }

    private String getAccessToken() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault()
                .createScoped("https://www.googleapis.com/auth/cloud-platform");
        credentials.refreshIfExpired();
        AccessToken token = credentials.getAccessToken();
        return token.getTokenValue();
    }
}
