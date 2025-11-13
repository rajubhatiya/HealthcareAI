package com.ai.healthcare.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class
TransactionRequest {
    @Schema(description = "Amount sent in Philippine Peso", example = "500.00")
    private double amountSent;

    @Schema(description = "Transfer fee in Philippine Peso", example = "30.00")
    private double transferFee;

    @Schema(description = "Sender's first name", example = "John")
    private String senderFirstName;

    @Schema(description = "Sender's last name", example = "Doe")
    private String senderLastName;

    @Schema(description = "Sender's address", example = "123 Main Street, Manila")
    private String senderAddress;

    @Schema(description = "Receiver's full name", example = "Maria Lopez")
    private String receiverName;

    @Schema(description = "Receiver's address", example = "456 Market Ave, Cebu")
    private String receiverAddress;

    @Schema(description = "Receiver's contact number", example = "09123456789")
    private String receiverContact;
}
