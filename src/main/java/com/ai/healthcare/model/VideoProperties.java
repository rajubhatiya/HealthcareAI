package com.ai.healthcare.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "openai.video")
public class VideoProperties {

    private String model;
    private String duration;
    private int pollAttempts;
    private long pollDelayMs;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
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