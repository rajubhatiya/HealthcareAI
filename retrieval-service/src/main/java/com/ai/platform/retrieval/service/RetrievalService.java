package com.ai.platform.retrieval.service;

import com.ai.platform.shared.EmbeddingJob;
import com.ai.platform.shared.Topics;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RetrievalService {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(RetrievalService.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final VectorStore vectorStore;
    private final ChatModel chatModel;

    public RetrievalService(KafkaTemplate<String, Object> kafkaTemplate, VectorStore vectorStore, ChatModel chatModel) {
        this.kafkaTemplate = kafkaTemplate;
        this.vectorStore = vectorStore;
        this.chatModel = chatModel;
    }

    @org.springframework.beans.factory.annotation.Value("${app.upload.dir:uploads}")
    private String uploadDir;

    public void ingest(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null)
            originalFilename = "unknown.pdf";

        // Save file locally (in a real app, use S3/Blob storage)
        // We use a configurable path so it works on both Windows and Docker (if volumes
        // are shared)
        Path uploadPath = Path.of(uploadDir).toAbsolutePath().normalize();
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Sanitize filename to prevent directory traversal
        String safeFilename = UUID.randomUUID() + "_" + originalFilename.replaceAll("[^a-zA-Z0-9.-]", "_");
        Path filePath = uploadPath.resolve(safeFilename);

        file.transferTo(filePath.toFile());
        logger.info("File saved to: {}", filePath);

        // Send event
        // We send the safeFilename relative to the upload dir, or the absolute path?
        // To be safe across different environments (if they share the same volume mount
        // path),
        // let's send both or just relying on the absolute path if they are on same FS.
        // BUT, if one is Windows and one is Docker, paths differ.
        // Strategy: The consumer (DocumentProcessor) also knows 'app.upload.dir'.
        // So we can send just the 'safeFilename' and let consumer resolve it against
        // its 'app.upload.dir'.
        // However, existing contract might expect full path.
        // Let's send the absolute path as 'documentPath', but also put 'filename' as
        // the safe filename.
        // The consumer will try 'documentPath' first, then fallback to 'app.upload.dir'
        // + 'safeFilename'.

        EmbeddingJob job = new EmbeddingJob(
                UUID.randomUUID(),
                filePath.toString(),
                safeFilename, // Sending the unique filename here so consumer can find it
                java.util.Collections.emptyMap());
        kafkaTemplate.send(Topics.DOCUMENT_INGESTION, job);
        logger.info("Ingestion event sent for file: {}, jobId: {}", originalFilename, job.jobId());
    }

    public String retrieveAndGenerate(String message) {
        logger.info("Searching for relevant documents...");
        List<Document> similarDocuments = vectorStore
                .similaritySearch(SearchRequest.builder().query(message).topK(2).build());
        logger.info("Found {} similar documents", similarDocuments.size());

        String context = similarDocuments.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n"));

        String promptStr = """
                You are a helpful assistant. Use the following context to answer the question.
                If the answer is not in the context, say you don't know.

                Context:
                {context}

                Question:
                {question}
                """;

        PromptTemplate promptTemplate = new PromptTemplate(promptStr);
        Prompt prompt = promptTemplate.create(Map.of("question", message, "context", context));

        String response = chatModel.call(prompt).getResult().getOutput().getText();
        logger.info("Generated response");
        return response;
    }
}
