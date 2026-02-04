package com.ai.healthcare.model;

import lombok.Builder;

public class VideoGenerationRequest {

    private String model;
    private String prompt;
    private String duration;

    public VideoGenerationRequest() {
    }

    public VideoGenerationRequest(String model, String prompt, String duration) {
        this.model = model;
        this.prompt = prompt;
        this.duration = duration;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters and Setters
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public static class Builder {
        private String model;
        private String prompt;
        private String duration;

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder prompt(String prompt) {
            this.prompt = prompt;
            return this;
        }

        public Builder duration(String duration) {
            this.duration = duration;
            return this;
        }

        public VideoGenerationRequest build() {
            return new VideoGenerationRequest(model, prompt, duration);
        }
    }
}
