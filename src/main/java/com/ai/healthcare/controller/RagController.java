package com.ai.healthcare.controller;

import com.ai.healthcare.service.RagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/rag")
public class RagController {

    private final RagService ragService;

    public RagController(RagService ragService) {
        this.ragService = ragService;
    }

    @PostMapping("/ingest")
    public ResponseEntity<String> ingest(@RequestParam("file") MultipartFile file) {
        try {
            ragService.ingest(file);
            return ResponseEntity.ok("Document ingested successfully");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error ingestion document: " + e.getMessage());
        }
    }

    @GetMapping("/query")
    public ResponseEntity<String> query(@RequestParam String question) {
        String response = ragService.retrieveAndGenerate(question);
        return ResponseEntity.ok(response);
    }
}
