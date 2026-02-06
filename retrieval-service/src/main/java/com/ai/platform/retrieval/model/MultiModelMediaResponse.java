package com.ai.platform.retrieval.model;

public class MultiModelMediaResponse {
    private String imageUrl;
    private String videoUrl;
    private String videoStatus;

    public MultiModelMediaResponse() {
    }

    public MultiModelMediaResponse(String imageUrl, String videoUrl, String videoStatus) {
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.videoStatus = videoStatus;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoStatus() {
        return videoStatus;
    }

    public void setVideoStatus(String videoStatus) {
        this.videoStatus = videoStatus;
    }

    public static class Builder {
        private String imageUrl;
        private String videoUrl;
        private String videoStatus;

        public Builder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder videoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
            return this;
        }

        public Builder videoStatus(String videoStatus) {
            this.videoStatus = videoStatus;
            return this;
        }

        public MultiModelMediaResponse build() {
            return new MultiModelMediaResponse(imageUrl, videoUrl, videoStatus);
        }
    }
}
