package com.geektext.bookbrowsingsorting.service;

import com.geektext.bookbrowsingsorting.model.Book;
import com.geektext.bookbrowsingsorting.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getBooksByGenre(String genre) {
        return bookRepository.findByGenreIgnoreCase(genre);
    }

    public List<Book> getTopSellers() {
        return bookRepository.findTop10ByOrderByCopiesSoldDesc();
    }

    public List<Book> getBooksByRating(BigDecimal rating) {
        return bookRepository.findByRatingGreaterThanEqual(rating);
    }

    public void applyDiscount(String publisher, double discountPercent) {

        List<Book> books = bookRepository.findByPublisherIgnoreCase(publisher);

        BigDecimal discountRate = BigDecimal.valueOf(discountPercent)
                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);

        for (Book book : books) {

            BigDecimal discount = book.getPrice().multiply(discountRate);

            BigDecimal newPrice = book.getPrice()
                    .subtract(discount)
                    .setScale(2, RoundingMode.HALF_UP);

            book.setPrice(newPrice);
        }

        bookRepository.saveAll(books);
    }
}