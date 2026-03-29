package com.geektext.services;

import com.geektext.dto.BookDto;
import com.geektext.exceptions.ApiException;
import jakarta.annotation.PostConstruct;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private final CartStorage cartStorage;
    private ConcurrentHashMap<String, List<BookDto>> carts = new ConcurrentHashMap<>();

    public CartService(CartStorage cartStorage) {
        this.cartStorage = cartStorage;
    }

    @PostConstruct
    public void init() {
        carts = cartStorage.loadCarts();
    }

    public List<BookDto> getCart(String userId) {
        validateUserId(userId);
        return carts.getOrDefault(userId, List.of());
    }

    public boolean addToCart(String userId, BookDto book) {
        validateUserId(userId);

        final boolean[] added = {false};

        carts.compute(userId, (k, v) -> {
            List<BookDto> cart = (v == null) ? new ArrayList<>() : new ArrayList<>(v);

            boolean alreadyExists = cart.stream()
                    .anyMatch(b -> b.getId().equals(book.getId()));

            if (!alreadyExists) {
                cart.add(book);
                added[0] = true;
            }

            return cart;
        });

        cartStorage.saveCarts(carts);
        return added[0];
    }

    public void removeFromCart(String userId, String bookId) {
        validateUserId(userId);

        carts.compute(userId, (k, v) -> {
            if (v == null) {
                throw new ApiException(HttpStatus.NOT_FOUND, "Cart is empty.");
            }

            List<BookDto> updated = new ArrayList<>(v);
            boolean removed = updated.removeIf(b -> b.getId().equals(bookId));

            if (!removed) {
                throw new ApiException(HttpStatus.NOT_FOUND, "Book not found in cart.");
            }

            return updated;
        });

        cartStorage.saveCarts(carts);
    }

    public BigDecimal subtotal(String userId) {
        validateUserId(userId);

        return getCart(userId).stream()
                .map(b -> b.getPrice() == null ? BigDecimal.ZERO : b.getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void validateUserId(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "User ID is required.");
        }
    }
}