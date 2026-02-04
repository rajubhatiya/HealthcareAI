package com.ai.healthcare.service;

import com.ai.healthcare.model.MultiModelMediaResponse;

public interface MultiModelAIService {
    String generateHealthyDiatImage(String prompt);
    MultiModelMediaResponse generateImage(String prompt);

    MultiModelMediaResponse generateVideo(String prompt);
}
