package com.geektext.controllers;

import com.geektext.dto.AddToCartRequest;
import com.geektext.dto.BookDto;
import com.geektext.services.BooksApiService;
import com.geektext.services.CartService;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final BooksApiService booksApiService;

    public CartController(CartService cartService, BooksApiService booksApiService) {
        this.cartService = cartService;
        this.booksApiService = booksApiService;
    }

    @GetMapping("/{userId}")
    public List<BookDto> getCart(@PathVariable String userId) {
        return cartService.getCart(userId);
    }

    @PostMapping("/{userId}/items")
    public Map<String, Object> addToCart(@PathVariable String userId,
                                         @Valid @RequestBody AddToCartRequest body) {
        BookDto book = booksApiService.fetchBookById(body.getBookId());
        boolean added = cartService.addToCart(userId, book);

        if (!added) {
            return Map.of(
                    "message", "Book already exists in cart.",
                    "cart", cartService.getCart(userId)
            );
        }

        return Map.of(
                "message", "Book added to cart.",
                "cart", cartService.getCart(userId)
        );
    }

    @DeleteMapping("/{userId}/items/{bookId}")
    public Map<String, Object> removeFromCart(@PathVariable String userId,
                                              @PathVariable String bookId) {
        cartService.removeFromCart(userId, bookId);
        return Map.of(
                "message", "Book removed from cart.",
                "cart", cartService.getCart(userId)
        );
    }

    @GetMapping("/{userId}/subtotal")
    public Map<String, Object> subtotal(@PathVariable String userId) {
        BigDecimal subtotal = cartService.subtotal(userId);
        return Map.of(
                "userId", userId,
                "subtotal", subtotal
        );
    }
}