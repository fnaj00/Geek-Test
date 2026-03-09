package com.geektext.bookbrowsingsorting.model;

import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class Book {
    @Id
    private String isbn;

    @Column(nullable = false)
    private String bookTitle;

    private String author;
    private String genre;


    private String publisher;

    private Double price;
    private Double rating;
    private Integer copiesSold;

    public Book() {}

    public Book(String isbn, String bookTitle, String author, String genre,
                String publisher, Double price, Double rating, Integer copiesSold) {
        this.isbn = isbn;
        this.bookTitle = bookTitle;
        this.author = author;
        this.genre = genre;
        this.publisher = publisher;
        this.price = price;
        this.rating = rating;
        this.copiesSold = copiesSold;
    }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }

    public Integer getCopiesSold() { return copiesSold; }
    public void setCopiesSold(Integer copiesSold) { this.copiesSold = copiesSold; }
}
