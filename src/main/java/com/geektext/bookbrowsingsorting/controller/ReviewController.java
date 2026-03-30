package com.geektext.bookbrowsingsorting.controller;

import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final List<String> reviews = new ArrayList<>();

    // 1) Add a review
    @PostMapping("/add")
    public String addReview(@RequestBody String review) {
        if (review == null || review.trim().isEmpty()) {
            return "Review cannot be empty";
        }

        reviews.add(review);
        return "Review added successfully!";
    }

    // 2) Get all reviews
    @GetMapping
    public List<String> getAllReviews() {
        return reviews;
    }

    // 3) Get review count
    @GetMapping("/count")
    public int getReviewCount() {
        return reviews.size();
    }

    // 4) Delete all reviews
    @DeleteMapping
    public String clearReviews() {
        reviews.clear();
        return "All reviews cleared!";
    }
}