package com.ai.healthcare.service;

import com.ai.healthcare.model.MultiModelMediaResponse;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of the MultiModelAIService.
 * Orchestrates the generation of images and videos using underlying AI clients.
 */
@Service
public class MultiModelAIServiceImpl implements MultiModelAIService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MultiModelAIServiceImpl.class);

    @Autowired
    private OpenAiImageModel openAiImageModel;

    @Autowired
    private OpenAIVideoClient videoClient;

    /**
     * Generates an image based on a prompt, specifically tailored for healthy diet
     * visualization.
     *
     * @param prompt The prompt for image generation.
     * @return The URL of the generated image.
     */
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

    /**
     * Generates a generic image based on a prompt.
     *
     * @param prompt The prompt for image generation.
     * @return A response object containing the image URL.
     */
    @Override
    public MultiModelMediaResponse generateImage(String prompt) {
        String imageUrl = generateHealthyDiatImage(prompt);
        return MultiModelMediaResponse.builder()
                .imageUrl(imageUrl)
                .build();
    }

    /**
     * Generates a video based on a prompt.
     * Handles exceptions gracefully and returns a status indicating success or
     * failure.
     *
     * @param prompt The prompt for video generation.
     * @return A response object containing the video URL and status.
     */
    @Override
    public MultiModelMediaResponse generateVideo(String prompt) {

        log.info("Generating video for prompt: {}", prompt);

        try {

            String videoUrl = videoClient.generateVideo(prompt);

            log.info("Video generation successful. URL: {}", videoUrl);

            return MultiModelMediaResponse.builder()
                    .videoUrl(videoUrl)
                    .videoStatus("COMPLETED")
                    .build();

        } catch (Exception e) {

            log.error("Video generation service failed for prompt: " + prompt, e);

            return MultiModelMediaResponse.builder()
                    .videoStatus("FAILED")
                    // We can include a generic failure message if the model supports it,
                    // or just rely on the status.
                    .build();
        }
    }

}
