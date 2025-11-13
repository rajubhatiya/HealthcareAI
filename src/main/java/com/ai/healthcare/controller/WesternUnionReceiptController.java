package com.ai.healthcare.controller;

import com.ai.healthcare.model.TransactionRequest;
import com.ai.healthcare.service.OllamaService;
import com.ai.healthcare.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/wu")
public class WesternUnionReceiptController {

    @Autowired
    private OllamaService ollamaService;

    @Autowired
    private PdfService pdfService;


    @PostMapping(value = "/v1/receipt", produces = MediaType.APPLICATION_PDF_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> generateReceiptV1(@RequestBody TransactionRequest transactionRequest) {
        try {
            String transactionId = UUID.randomUUID().toString();
            String aiReceiptText = ollamaService.generateWesternUnionReceiptV1(transactionId, transactionRequest);
            byte[] pdfBytes = pdfService.createPdf(aiReceiptText);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment", "western_union_receipt.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
