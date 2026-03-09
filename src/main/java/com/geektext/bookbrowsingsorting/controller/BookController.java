package com.geektext.bookbrowsingsorting.controller;

import com.geektext.bookbrowsingsorting.model.Book;
import com.geektext.bookbrowsingsorting.repository.BookRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Sort;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookRepository repo;

    public BookController(BookRepository repo) {
        this.repo = repo;
    }

    // GET http://localhost:8080/api/books/genre?Genre=Fantasy
    @GetMapping("/genre")
    public List<Book> getBooksByGenre(@RequestParam(name = "Genre") String genre) {
        return repo.findByGenreIgnoreCase(genre);
    }

    // GET http://localhost:8080/api/books?sort=price
    @GetMapping
    public List<Book> getBooksSorted(
            @RequestParam String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort.Direction dir = direction.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        return repo.findAll(Sort.by(dir, sort));
    }

    // GET http://localhost:8080/api/books/top-sellers
    @GetMapping("/top-sellers")
    public List<Book> getTopSellers() {
        return repo.findTop10ByOrderByCopiesSoldDesc();
    }

    // GET http://localhost:8080/api/books/rating?Rating=4.5
    @GetMapping("/rating")
    public List<Book> getBooksByRating(@RequestParam(name = "Rating") Double rating) {
        return repo.findByRatingGreaterThanEqual(rating);
    }

    // PATCH http://localhost:8080/api/books/discount?publisher=Penguin&discountPercent=10
    @PatchMapping("/discount")
    public List<Book> discountBooksByPublisher(
            @RequestParam String publisher,
            @RequestParam Double discountPercent
    ) {

        List<Book> books = repo.findByPublisherIgnoreCase(publisher);

        for (Book book : books) {

            double discountAmount = book.getPrice() * (discountPercent / 100);
            book.setPrice(book.getPrice() - discountAmount);

            repo.save(book);
        }

        return books;
    }
}