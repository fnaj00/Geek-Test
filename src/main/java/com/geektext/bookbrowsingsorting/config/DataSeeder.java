package com.geektext.bookbrowsingsorting.config;

import com.geektext.bookbrowsingsorting.model.Book;
import com.geektext.bookbrowsingsorting.repository.BookRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.math.BigDecimal;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedBooks(BookRepository repo) {
        return args -> {
            if (repo.count() > 0) {
                return;
            }

            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("mock_data.csv");

            if (inputStream == null) {
                throw new RuntimeException("Could not find mock_data.csv in src/main/resources");
            }

            try (
                    InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                    CSVParser csvParser = new CSVParser(
                            reader,
                            CSVFormat.DEFAULT
                                    .builder()
                                    .setHeader()
                                    .setSkipHeaderRecord(true)
                                    .build()
                    )
            ) {
                for (CSVRecord record : csvParser) {
                    String isbn = record.get("isbn").trim();
                    String bookTitle = record.get("book_title").trim();
                    String author = record.get("author").trim();
                    String genre = record.get("genre").trim();
                    String publisher = record.get("publisher").trim();
                    Double price = Double.parseDouble(record.get("price").trim());
                    Double rating = Double.parseDouble(record.get("rating").trim());
                    Integer copiesSold = Integer.parseInt(record.get("copies_sold").trim());

                    Book book = new Book(
                            Long.parseLong(isbn.replaceAll("[^0-9]", "")),
                            bookTitle,
                            "No description",
                            BigDecimal.valueOf(price),
                            author,
                            genre,
                            publisher,
                            2020,
                            copiesSold,
                            BigDecimal.valueOf(rating)
                    );

                    repo.save(book);
                }
            }
        };
    }
}