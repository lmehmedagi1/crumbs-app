package com.crumbs.reviewservice.services;

import com.crumbs.reviewservice.exceptions.ReviewNotFoundException;
import com.crumbs.reviewservice.models.Review;
import com.crumbs.reviewservice.repositories.ReviewRepository;
import com.crumbs.reviewservice.requests.ReviewRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Service
@Validated
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Transactional(readOnly = true)
    public Review getReview(@NotNull UUID id) {
        return reviewRepository.findById(id).orElseThrow(ReviewNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<Review> getReviewsOfUser(@NotNull UUID userId) {
        return reviewRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Review> getReviewsOfRecipe(@NotNull UUID recipeId) {
        return reviewRepository.findByRecipeId(recipeId);
    }

    @Transactional(readOnly = true)
    public Double getRecipeRating(@NotNull UUID recipeId) {
        return (double) Math.round(reviewRepository.getAvgRatingOfRecipe(recipeId) * 100) / 100;
    }

    private void modifyReview(ReviewRequest reviewRequest, UUID userId, Review review) {
        review.setUserId(userId);
        review.setRecipeId(UUID.fromString(reviewRequest.getRecipe_id()));
        review.setIsLiked(reviewRequest.getIs_liked());
        review.setRating(reviewRequest.getRating());
        review.setComment(reviewRequest.getComment());
    }

    @Transactional
    public Review saveReview(@NotNull @Valid ReviewRequest reviewRequest, @NotNull UUID userId) {
        Review review = new Review();
        modifyReview(reviewRequest, userId, review);
        return reviewRepository.save(review);
    }

    @Transactional
    public Review updateReview(@NotNull @Valid ReviewRequest reviewRequest, @NotNull UUID id, @NotNull UUID userId) {
        return reviewRepository.findById(id).map(review -> {
            modifyReview(reviewRequest, userId,  review);
            return reviewRepository.save(review);
        }).orElseThrow(ReviewNotFoundException::new);
    }

    @Transactional
    public void updateReview(@NotNull @Valid Review updatedReview) {
        reviewRepository.save(updatedReview);
    }

    @Transactional
    public void deleteReview(@NotNull UUID id) {
        if (!reviewRepository.existsById(id))
            throw new ReviewNotFoundException();
        reviewRepository.deleteById(id);
    }
}
