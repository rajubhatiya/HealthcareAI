package com.ai.healthcare.service;

import com.ai.healthcare.model.TransactionRequest;
import com.ai.healthcare.utility.WuUtilities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OllamaService {


    @Value("${OPENAI_API_KEY:NOT_FOUND}")
    private String openAiApiKey;

    @Value("${spring.ai.openai.chat.option.model}")
    private String openAiModel;

    @Value("${openai.api.url}")
    private String apiUrl;

    private final ChatClient openAi;

    private final RestTemplate restTemplate = new RestTemplate();

    public OllamaService(@Qualifier("openAiChatClient") ChatClient openAi) {
        this.openAi = openAi;
    }


    public String generateWesternUnionReceiptV1(String transactionId, TransactionRequest transactionRequest) {
        String prompt = String.format("""
                        Generate a Western Union payment receipt with the following details:
                        Money Transfer Control Number(MTCN): %s,
                        Date: %s
                        Amount Sent: %.2f ₱(Philippine Peso)
                        Transfer Fee: %.2f ₱(Philippine Peso)
                        Sender First Name: %s
                        Sender Last Name: %s
                        Sender Address: %s
                        Receiver Name: %s
                        Receiver Address: %s
                        Receiver Contact: %s
                        """, WuUtilities.generateMTCN(transactionId, 5), WuUtilities.getCurrentDate(transactionId), transactionRequest.getAmountSent(), transactionRequest.getTransferFee(), transactionRequest.getSenderFirstName(), transactionRequest.getSenderLastName(), transactionRequest.getSenderAddress(),
                transactionRequest.getReceiverName(), transactionRequest.getReceiverAddress(), transactionRequest.getReceiverContact());




        String reqBody = String.format("""
                {
                    "model": "gpt-4o",
                    "prompt": "%s",
                    "stream": false
                }
                """, prompt.replace("\"", "\\\""), prompt.replace("\n", "")); // Escape quotes


        return openAi.prompt()
                .user(prompt)
                .options(ChatOptions.builder()
                        .model(openAiModel)
                        .temperature(1.0)
                        //      .maxTokens(2000)
                        .build())
                .call()
                .content();


    }
}
