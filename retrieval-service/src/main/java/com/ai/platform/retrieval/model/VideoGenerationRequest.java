package com.ai.platform.retrieval.model;

public class VideoGenerationRequest {
    private String model;
    private String prompt;
    private int duration;

    public VideoGenerationRequest(String model, String prompt, int duration) {
        this.model = model;
        this.prompt = prompt;
        this.duration = duration;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getModel() {
        return model;
    }

    public String getPrompt() {
        return prompt;
    }

    public int getDuration() {
        return duration;
    }

    public static class Builder {
        private String model;
        private String prompt;
        private int duration;

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder prompt(String prompt) {
            this.prompt = prompt;
            return this;
        }

        public Builder duration(int duration) {
            this.duration = duration;
            return this;
        }

        public VideoGenerationRequest build() {
            return new VideoGenerationRequest(model, prompt, duration);
        }
    }
}
