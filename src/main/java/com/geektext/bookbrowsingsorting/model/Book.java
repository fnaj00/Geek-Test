package com.geektext.bookbrowsingsorting.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @Column(name = "isbn", nullable = false, unique = true)
    private Long isbn;

    @Column(name = "book_title", nullable = false)
    private String bookTitle;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private String author;

    private String genre;

    private String publisher;

    @Column(name = "year_published")
    private Integer yearPublished;

    @Column(name = "copies_sold")
    private Integer copiesSold;

    @Column(precision = 2, scale = 1)
    private BigDecimal rating;

    public Book() {}

    public Book(Long isbn, String bookTitle, String description,
                BigDecimal price, String author, String genre,
                String publisher, Integer yearPublished,
                Integer copiesSold, BigDecimal rating) {
        this.isbn = isbn;
        this.bookTitle = bookTitle;
        this.description = description;
        this.price = price;
        this.author = author;
        this.genre = genre;
        this.publisher = publisher;
        this.yearPublished = yearPublished;
        this.copiesSold = copiesSold;
        this.rating = rating;
    }

    public Long getIsbn() {
        return isbn;
    }

    public void setIsbn(Long isbn) {
        this.isbn = isbn;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Integer getYearPublished() {
        return yearPublished;
    }

    public void setYearPublished(Integer yearPublished) {
        this.yearPublished = yearPublished;
    }

    public Integer getCopiesSold() {
        return copiesSold;
    }

    public void setCopiesSold(Integer copiesSold) {
        this.copiesSold = copiesSold;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }
}