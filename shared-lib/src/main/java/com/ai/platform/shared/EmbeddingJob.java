package com.ai.platform.shared;

import java.util.UUID;

public record EmbeddingJob(
                UUID jobId,
                String documentPath,
                String filename,
                java.util.Map<String, Object> metadata) {
}
