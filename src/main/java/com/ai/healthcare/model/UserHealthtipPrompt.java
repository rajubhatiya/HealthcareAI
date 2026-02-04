package com.ai.healthcare.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

public class UserHealthtipPrompt {

    @NotNull(message = "Message cannot be empty")
    private String message;

    @NotNull(message = "Treatment type is required")
    private TreatmentType treatmentType;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TreatmentType getTreatmentType() {
        return treatmentType;
    }

    public void setTreatmentType(TreatmentType treatmentType) {
        this.treatmentType = treatmentType;
    }
}