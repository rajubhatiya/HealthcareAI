package com.ai.platform.retrieval.service;

import com.ai.platform.retrieval.model.MultiModelMediaResponse;

public interface MultiModelAIService {
    String generateHealthyDiatImage(String prompt);

    MultiModelMediaResponse generateImage(String prompt);

    MultiModelMediaResponse generateVideo(String prompt);
}
