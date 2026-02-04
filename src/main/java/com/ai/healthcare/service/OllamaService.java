package com.ai.healthcare.service;

import com.ai.healthcare.model.TransactionRequest;
import com.ai.healthcare.utility.WuUtilities;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Service class responsible for interacting with AI models to generate content.
 * <p>
 * This service handles the generation of Western Union style receipts by
 * prompting
 * an AI model (configured via ChatClient).
 * </p>
 */
@Service
public class OllamaService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(OllamaService.class);

    // API Key for OpenAI, injected from application properties with a default value
    @Value("${OPENAI_API_KEY:NOT_FOUND}")
    private String openAiApiKey;

    // The specific AI model to be used for chat completions (e.g., gpt-3.5-turbo,
    // gpt-4)
    @Value("${spring.ai.openai.chat.option.model}")
    private String openAiModel;

    // Base URL for the OpenAI API
    @Value("${openai.api.url}")
    private String apiUrl;

    // The ChatClient interface for interacting with the AI service
    private final ChatClient openAi;

    // RestTemplate for making HTTP requests (currently unused in the provided
    // methods)
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Constructor injecting the specific ChatClient bean.
     *
     * @param openAi The ChatClient bean qualified as 'openAiChatClient'.
     */
    public OllamaService(@Qualifier("openAiChatClient") ChatClient openAi) {
        this.openAi = openAi;
    }

    /**
     * Generates a receipt based on transaction details.
     *
     * @param transactionId      The unique identifier for the transaction.
     * @param transactionRequest The request object containing sender, receiver, and
     *                           amount details.
     * @return A string containing the generated receipt text from the AI model.
     */
    public String generateWesternUnionReceiptV1(String transactionId, TransactionRequest transactionRequest) {
        log.info("Generating Western Union receipt for transactionId: {}", transactionId);

        // Construct the prompt with dynamic transaction details using String.format
        String prompt = String.format("""
                Generate a payment receipt with the following details:
                Money Transfer Control Number(MTCN): %s,
                Date: %s
                Amount Sent: %.2f R(Indian Rupee)
                Transfer Fee: %.2f R(Indian Rupee)
                Sender First Name: %s
                Sender Last Name: %s
                Sender Address: %s
                Receiver Name: %s
                Receiver Address: %s
                Receiver Contact: %s
                """, WuUtilities.generateMTCN(transactionId, 5), WuUtilities.getCurrentDate(transactionId),
                transactionRequest.getAmountSent(), transactionRequest.getTransferFee(),
                transactionRequest.getSenderFirstName(), transactionRequest.getSenderLastName(),
                transactionRequest.getSenderAddress(),
                transactionRequest.getReceiverName(), transactionRequest.getReceiverAddress(),
                transactionRequest.getReceiverContact());

        log.debug("Generated prompt for receipt: {}", prompt);

        // Call the AI model with the constructed prompt and specific options
        String response = openAi.prompt()
                .user(prompt)
                .options(ChatOptions.builder()
                        .model(openAiModel)
                        .temperature(1.0)
                        // .maxTokens(2000)
                        .build())
                .call()
                .content();

        log.info("Successfully generated receipt for transactionId: {}", transactionId);
        return response;
    }
}
