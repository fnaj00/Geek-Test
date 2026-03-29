package com.geektext.dto;

import java.math.BigDecimal;

public class BookDto {
    private String id;
    private String title;
    private BigDecimal price;
    private Boolean availability;

    public BookDto() {}

    public BookDto(String id, String title, BigDecimal price, Boolean availability) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.availability = availability;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean getAvailability() {
        return availability;
    }

    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }
}