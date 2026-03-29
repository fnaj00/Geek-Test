package com.geektext.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geektext.dto.BookDto;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CartStorage {

    private static final String FILE_NAME = "cart-data.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ConcurrentHashMap<String, List<BookDto>> loadCarts() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return new ConcurrentHashMap<>();
        }

        try {
            Map<String, List<BookDto>> loaded = objectMapper.readValue(
                    file,
                    new TypeReference<Map<String, List<BookDto>>>() {}
            );
            return new ConcurrentHashMap<>(loaded);
        } catch (IOException e) {
            return new ConcurrentHashMap<>();
        }
    }

    public void saveCarts(Map<String, List<BookDto>> carts) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_NAME), carts);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save cart data.");
        }
    }
}