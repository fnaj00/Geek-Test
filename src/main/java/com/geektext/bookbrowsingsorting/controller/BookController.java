package com.geektext.bookbrowsingsorting.controller;

import com.geektext.bookbrowsingsorting.model.Book;
import com.geektext.bookbrowsingsorting.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/genre/{genre}")
    public List<Book> getBooksByGenre(@PathVariable String genre) {
        return bookService.getBooksByGenre(genre);
    }

    @GetMapping("/top-sellers")
    public List<Book> getTopSellers() {
        return bookService.getTopSellers();
    }

    @GetMapping("/rating/{rating}")
    public List<Book> getBooksByRating(@PathVariable BigDecimal rating) {
        return bookService.getBooksByRating(rating);
    }

    @PatchMapping("/discount")
    public String applyDiscount(
            @RequestParam String publisher,
            @RequestParam double discountPercent) {

        bookService.applyDiscount(publisher, discountPercent);
        return "Discount applied successfully to books from publisher: " + publisher;
    }
}