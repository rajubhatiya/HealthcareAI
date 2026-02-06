package com.ai.platform.embedding.worker;

import com.ai.platform.shared.DocumentChunk;
import com.ai.platform.shared.Topics;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class EmbeddingWorker {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(EmbeddingWorker.class);

    private final VectorStore vectorStore;

    public EmbeddingWorker(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @KafkaListener(topics = Topics.EMBEDDING_REQUESTS, groupId = "embedding-service-group")
    public void processEmbeddingRequest(DocumentChunk chunk) {
        logger.info("Generating embedding for chunk: {}", chunk.id());
        try {
            // Create Document
            Document document = new Document(chunk.content(), chunk.metadata());

            // Add to VectorStore (this triggers embedding generation)
            vectorStore.add(List.of(document));

            logger.info("Successfully generated and stored embedding for chunk: {} with content length: {}", chunk.id(),
                    chunk.content().length());
        } catch (Exception e) {
            logger.error("Error processing embedding request", e);
        }
    }
}
