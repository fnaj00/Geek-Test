package com.geektext.services;

import com.geektext.dto.BookDto;
import com.geektext.exceptions.ApiException;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class BooksApiService {

    private final List<BookDto> books = List.of(
            new BookDto("1", "Clean Code", new BigDecimal("29.99"), true),
            new BookDto("2", "Design Patterns", new BigDecimal("39.99"), true),
            new BookDto("3", "The Pragmatic Programmer", new BigDecimal("24.99"), true)
    );

    public List<BookDto> fetchBooksByGenre(String genre) {
        return books;
    }

    public BookDto fetchBookById(String bookId) {
        return books.stream()
                .filter(b -> b.getId().equals(bookId))
                .findFirst()
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Book not found."));
    }
}