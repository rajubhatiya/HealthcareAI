package com.ai.healthcare.service;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RagService {

    private final VectorStore vectorStore;
    private final ChatModel chatModel;

    @Value("classpath:/rag-prompt-template.st")
    private Resource ragPromptTemplate;

    public RagService(VectorStore vectorStore, @Qualifier("openAiChatModel") ChatModel chatModel) {
        this.vectorStore = vectorStore;
        this.chatModel = chatModel;
    }

    public void ingest(MultipartFile file) throws IOException {
        Path tempFile = Files.createTempFile("rag-upload", ".pdf");
        file.transferTo(tempFile.toFile());

        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(tempFile.toUri().toString());
        List<Document> documents = pdfReader.get();

        vectorStore.add(documents);

        // Cleanup
        Files.deleteIfExists(tempFile);
    }

    public String retrieveAndGenerate(String message) {
        List<Document> similarDocuments = vectorStore
                .similaritySearch(SearchRequest.builder().query(message).topK(2).build());
        List<String> contentList = similarDocuments.stream()
                .map(Document::getText)
                .collect(Collectors.toList());
        String context = String.join("\n", contentList);

        PromptTemplate promptTemplate = new PromptTemplate(getPromptTemplate());
        Prompt prompt = promptTemplate.create(Map.of("question", message, "context", context));

        return chatModel.call(prompt).getResult().getOutput().getText();
    }

    private String getPromptTemplate() {
        return """
                You are a helpful assistant. Use the following context to answer the question.
                If the answer is not in the context, say you don't know not try to hallucinate or makeup assumptions.

                Context:
                {context}

                Question:
                {question}
                """;
    }
}
