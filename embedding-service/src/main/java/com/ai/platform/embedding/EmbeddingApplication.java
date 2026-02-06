package com.ai.platform.embedding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@org.springframework.context.annotation.Import(com.ai.platform.shared.config.KafkaTopicConfig.class)
public class EmbeddingApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmbeddingApplication.class, args);
    }
}
