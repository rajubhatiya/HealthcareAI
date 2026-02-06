package com.ai.platform.retrieval.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "openai.video")
public class VideoProperties {
    private String model = "text-to-video-model";
    private int duration = 5;
    private int pollAttempts = 20;
    private long pollDelayMs = 2000;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPollAttempts() {
        return pollAttempts;
    }

    public void setPollAttempts(int pollAttempts) {
        this.pollAttempts = pollAttempts;
    }

    public long getPollDelayMs() {
        return pollDelayMs;
    }

    public void setPollDelayMs(long pollDelayMs) {
        this.pollDelayMs = pollDelayMs;
    }
}
