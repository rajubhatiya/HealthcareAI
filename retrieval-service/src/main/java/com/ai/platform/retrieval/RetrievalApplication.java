package com.ai.platform.retrieval;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@org.springframework.context.annotation.Import(com.ai.platform.shared.config.KafkaTopicConfig.class)
public class RetrievalApplication {
    public static void main(String[] args) {
        SpringApplication.run(RetrievalApplication.class, args);
    }
}
