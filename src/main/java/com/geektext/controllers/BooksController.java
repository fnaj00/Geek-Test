package com.geektext.controllers;

import com.geektext.dto.BookDto;
import com.geektext.services.BooksApiService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BooksController {

    private final BooksApiService booksApiService;

    public BooksController(BooksApiService booksApiService) {
        this.booksApiService = booksApiService;
    }

    @GetMapping
    public List<BookDto> getBooks(@RequestParam(required = false) String genre) {
        return booksApiService.fetchBooksByGenre(genre);
    }
}