package com.sw1project.profilemanagement.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateCreditCardRequest {

    @NotBlank(message = "cardNumber is required")
    private String cardNumber;

    @NotBlank(message = "expirationDate is required")
    private String expirationDate;

    @NotBlank(message = "cardHolderName is required")
    private String cardHolderName;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }
}