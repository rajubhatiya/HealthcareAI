package com.ai.platform.retrieval.controller;

import com.ai.platform.retrieval.service.RetrievalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rag")
public class RetrievalController {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(RetrievalController.class);
    private final RetrievalService retrievalService;

    public RetrievalController(RetrievalService retrievalService) {
        this.retrievalService = retrievalService;
    }

    @PostMapping("/ingest")
    public ResponseEntity<String> ingest(@RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        logger.info("Received ingestion request for file: {}", file.getOriginalFilename());
        try {
            retrievalService.ingest(file);
            return ResponseEntity.ok("Document ingestion started.");
        } catch (java.io.IOException e) {
            logger.error("Failed to ingest document", e);
            return ResponseEntity.internalServerError().body("Failed to ingest document: " + e.getMessage());
        }
    }

    @GetMapping("/query")
    public ResponseEntity<String> query(@RequestParam String question) {
        logger.info("Received query: {}", question);
        String response = retrievalService.retrieveAndGenerate(question);
        return ResponseEntity.ok(response);
    }
}
