package com.crumbs.reviewservice.controllers;

import com.crumbs.reviewservice.exceptions.ReviewNotFoundException;
import com.crumbs.reviewservice.model.Review;
import com.crumbs.reviewservice.requests.CreateReviewRequest;
import com.crumbs.reviewservice.requests.UpdateReviewRequest;
import com.crumbs.reviewservice.response.ReviewResponse;
import com.crumbs.reviewservice.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/reviews")
    public ResponseEntity<List<Review>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @GetMapping("/review")
    public ResponseEntity<Review> getReview(String id) {
        return ResponseEntity.ok(reviewService.getReview(id));
    }

    @PostMapping("/reviews/create")
    public ResponseEntity<ReviewResponse> createReview(@RequestBody @Valid CreateReviewRequest createReviewRequest) {
        try {
            final Review review = reviewService.saveReview(createReviewRequest);
            return ResponseEntity.ok(new ReviewResponse(review));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PatchMapping("/reviews/update")
    public ResponseEntity<ReviewResponse> updateReview(@RequestBody @Valid UpdateReviewRequest updateReviewRequest) {
        try {
            final Review review = reviewService.updateReview(updateReviewRequest);
            return ResponseEntity.ok(new ReviewResponse(review));
        } catch (ReviewNotFoundException reviewNotFoundException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, reviewNotFoundException.getMessage());
        }
    }

    @DeleteMapping("/reviews/delete")
    public void deleteReview(@RequestParam @Valid String id) {
        try {
            reviewService.deleteReview(id);
        } catch (ReviewNotFoundException reviewNotFoundException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, reviewNotFoundException.getMessage());
        }
    }

}
