package com.ai.platform.retrieval.model;

public class VideoStatusResponse {
    private String id;
    private String status;
    private VideoOutput output;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public VideoOutput getOutput() {
        return output;
    }

    public void setOutput(VideoOutput output) {
        this.output = output;
    }

    public static class VideoOutput {
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
