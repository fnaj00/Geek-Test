package com.geektext.dto;

import jakarta.validation.constraints.NotBlank;

public class AddToCartRequest {

    @NotBlank
    private String bookId;

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
}