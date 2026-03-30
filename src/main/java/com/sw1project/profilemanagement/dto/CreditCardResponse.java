package com.sw1project.profilemanagement.dto;

public class CreditCardResponse {

    private Long id;
    private String cardNumber;
    private String expirationDate;
    private String cardHolderName;
    private String username;

    public CreditCardResponse(Long id, String cardNumber, String expirationDate, String cardHolderName, String username) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.cardHolderName = cardHolderName;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public String getUsername() {
        return username;
    }
}