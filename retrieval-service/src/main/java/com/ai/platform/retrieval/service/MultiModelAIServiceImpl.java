package com.ai.platform.retrieval.service;

import com.ai.platform.retrieval.model.MultiModelMediaResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MultiModelAIServiceImpl implements MultiModelAIService {

    private static final Logger log = LoggerFactory.getLogger(MultiModelAIServiceImpl.class);

    private final OpenAiImageModel openAiImageModel;
    private final GeminiVideoClient geminiVideoClient;

    public MultiModelAIServiceImpl(OpenAiImageModel openAiImageModel,
            GeminiVideoClient geminiVideoClient) {
        this.openAiImageModel = openAiImageModel;
        this.geminiVideoClient = geminiVideoClient;
    }

    @Override
    public String generateHealthyDiatImage(String prompt) {
        ImageResponse response = openAiImageModel.call(new ImagePrompt(prompt, OpenAiImageOptions.builder()
                .height(1024)
                .quality("hd")
                .width(1024)
                .N(1)
                .build()));

        return response.getResult().getOutput().getUrl();
    }

    @Override
    public MultiModelMediaResponse generateImage(String prompt) {
        String imageUrl = generateHealthyDiatImage(prompt);
        return MultiModelMediaResponse.builder()
                .imageUrl(imageUrl)
                .build();
    }

    @Override
    public MultiModelMediaResponse generateVideo(String prompt) {
        log.info("Generating video for prompt using Gemini: {}", prompt);
        try {
            // Using Gemini/Vertex AI for video generation
            String videoUrl = geminiVideoClient.generateVideo(prompt);
            log.info("Gemini Video generation successful. URL/Data: {}", videoUrl);

            return MultiModelMediaResponse.builder()
                    .videoUrl(videoUrl)
                    .videoStatus("COMPLETED")
                    .build();

        } catch (Exception e) {
            log.error("Gemini Video generation failed for prompt: " + prompt, e);
            // Optional: fallback to OpenAI if needed, but for now reporting failure
            return MultiModelMediaResponse.builder()
                    .videoStatus("FAILED")
                    .build();
        }
    }
}
