package com.ai.platform.retrieval.service;

import org.springframework.web.multipart.MultipartFile;

public interface AudioMediaService {
    byte[] textToAudio(String text);

    String audioToText(MultipartFile audioFile);
}
