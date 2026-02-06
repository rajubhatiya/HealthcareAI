package com.ai.platform.shared;

import java.util.List;

public record DocumentChunk(
                String id,
                String content,
                java.util.Map<String, Object> metadata,
                List<Double> embedding) {
}
