package com.ai.platform.processor.worker;

import com.ai.platform.shared.DocumentChunk;
import com.ai.platform.shared.EmbeddingJob;
import com.ai.platform.shared.Topics;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Component
public class DocumentWorker {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DocumentWorker.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public DocumentWorker(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @org.springframework.beans.factory.annotation.Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @KafkaListener(topics = Topics.DOCUMENT_INGESTION, groupId = "document-processor-group")
    public void processDocument(EmbeddingJob job) {
        logger.info("Processing document: {}", job.documentPath());
        try {
            // Read PDF
            // Try absolute path first
            File file = new File(job.documentPath());
            if (!file.exists()) {
                logger.warn("File not found at absolute path: {}. Trying relative to upload dir: {}",
                        job.documentPath(), uploadDir);

                // Try resolving filename against uploadDir
                Path localPath = Path.of(uploadDir).resolve(job.filename()).toAbsolutePath().normalize();
                file = localPath.toFile();

                if (!file.exists()) {
                    logger.error("File not found at local path either: {}. Aborting job: {}", localPath, job.jobId());
                    return;
                }
                logger.info("Found file at local path: {}", localPath);
            }

            PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(
                    new org.springframework.core.io.FileSystemResource(file));
            List<Document> documents = pdfReader.get();

            // Split into chunks
            TokenTextSplitter splitter = new TokenTextSplitter();
            List<Document> chunks = splitter.apply(documents);

            for (Document chunk : chunks) {
                // Ensure metadata contains filename
                chunk.getMetadata().put("filename", job.filename());

                DocumentChunk documentChunk = new DocumentChunk(
                        UUID.randomUUID().toString(),
                        chunk.getText(),
                        chunk.getMetadata(),
                        List.of() // No embedding yet
                );
                kafkaTemplate.send(Topics.EMBEDDING_REQUESTS, documentChunk);
            }
            logger.info("Produced {} chunks for {}", chunks.size(), job.filename());

        } catch (Exception e) {
            logger.error("Error processing document", e);
        }
    }
}
