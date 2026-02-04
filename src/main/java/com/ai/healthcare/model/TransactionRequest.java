package com.ai.healthcare.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class TransactionRequest {
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

    public TransactionRequest() {
    }

    public TransactionRequest(double amountSent, double transferFee, String senderFirstName, String senderLastName,
            String senderAddress, String receiverName, String receiverAddress, String receiverContact) {
        this.amountSent = amountSent;
        this.transferFee = transferFee;
        this.senderFirstName = senderFirstName;
        this.senderLastName = senderLastName;
        this.senderAddress = senderAddress;
        this.receiverName = receiverName;
        this.receiverAddress = receiverAddress;
        this.receiverContact = receiverContact;
    }

    public static Builder builder() {
        return new Builder();
    }

    public double getAmountSent() {
        return amountSent;
    }

    public void setAmountSent(double amountSent) {
        this.amountSent = amountSent;
    }

    public double getTransferFee() {
        return transferFee;
    }

    public void setTransferFee(double transferFee) {
        this.transferFee = transferFee;
    }

    public String getSenderFirstName() {
        return senderFirstName;
    }

    public void setSenderFirstName(String senderFirstName) {
        this.senderFirstName = senderFirstName;
    }

    public String getSenderLastName() {
        return senderLastName;
    }

    public void setSenderLastName(String senderLastName) {
        this.senderLastName = senderLastName;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getReceiverContact() {
        return receiverContact;
    }

    public void setReceiverContact(String receiverContact) {
        this.receiverContact = receiverContact;
    }

    public static class Builder {
        private double amountSent;
        private double transferFee;
        private String senderFirstName;
        private String senderLastName;
        private String senderAddress;
        private String receiverName;
        private String receiverAddress;
        private String receiverContact;

        public Builder amountSent(double amountSent) {
            this.amountSent = amountSent;
            return this;
        }

        public Builder transferFee(double transferFee) {
            this.transferFee = transferFee;
            return this;
        }

        public Builder senderFirstName(String senderFirstName) {
            this.senderFirstName = senderFirstName;
            return this;
        }

        public Builder senderLastName(String senderLastName) {
            this.senderLastName = senderLastName;
            return this;
        }

        public Builder senderAddress(String senderAddress) {
            this.senderAddress = senderAddress;
            return this;
        }

        public Builder receiverName(String receiverName) {
            this.receiverName = receiverName;
            return this;
        }

        public Builder receiverAddress(String receiverAddress) {
            this.receiverAddress = receiverAddress;
            return this;
        }

        public Builder receiverContact(String receiverContact) {
            this.receiverContact = receiverContact;
            return this;
        }

        public TransactionRequest build() {
            return new TransactionRequest(amountSent, transferFee, senderFirstName, senderLastName, senderAddress,
                    receiverName, receiverAddress, receiverContact);
        }
    }
}
