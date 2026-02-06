package com.ai.platform.retrieval.service;

import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.audio.speech.SpeechModel;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class AudioMediaServiceImpl implements AudioMediaService {

    private final SpeechModel speechModel;
    private final OpenAiAudioTranscriptionModel transcriptionModel;

    public AudioMediaServiceImpl(SpeechModel speechModel, OpenAiAudioTranscriptionModel transcriptionModel) {
        this.speechModel = speechModel;
        this.transcriptionModel = transcriptionModel;
    }

    @Override
    public byte[] textToAudio(String text) {
        return speechModel.call(text);
    }

    @Override
    public String audioToText(MultipartFile audioFile) {
        try {
            ByteArrayResource resource = new ByteArrayResource(audioFile.getBytes()) {
                @Override
                public String getFilename() {
                    return audioFile.getOriginalFilename();
                }
            };
            return transcriptionModel.call(resource);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read audio file", e);
        }
    }
}
