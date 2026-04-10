package com.geektext.bookbrowsingsorting.repository;

import com.geektext.bookbrowsingsorting.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByGenreIgnoreCase(String genre);

    List<Book> findTop10ByOrderByCopiesSoldDesc();

    List<Book> findByRatingGreaterThanEqual(BigDecimal rating);

    List<Book> findByPublisherIgnoreCase(String publisher);
}