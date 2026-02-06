package com.ai.platform.shared.config;

import com.ai.platform.shared.Topics;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic documentIngestionTopic() {
        return TopicBuilder.name(Topics.DOCUMENT_INGESTION)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic embeddingRequestsTopic() {
        return TopicBuilder.name(Topics.EMBEDDING_REQUESTS)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic embeddingCompletedTopic() {
        return TopicBuilder.name(Topics.EMBEDDING_COMPLETED)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
