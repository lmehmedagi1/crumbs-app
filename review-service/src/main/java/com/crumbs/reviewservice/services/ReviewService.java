package com.crumbs.reviewservice.services;

import com.crumbs.reviewservice.exceptions.ReviewNotFoundException;
import com.crumbs.reviewservice.models.Review;
import com.crumbs.reviewservice.repositories.ReviewRepository;
import com.crumbs.reviewservice.requests.ReviewRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Transactional(readOnly = true)
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Review getReview(@NotBlank String id) throws ReviewNotFoundException {
        return reviewRepository.findById(UUID.fromString(id)).orElseThrow(() ->
                new ReviewNotFoundException(Review.class, "id", id));
    }

    private void modifyReview(ReviewRequest reviewRequest, Review review) {
        review.setUserId(UUID.fromString(reviewRequest.getUser_id()));
        review.setRecipeId(UUID.fromString(reviewRequest.getRecipe_id()));
        review.setIsLiked(reviewRequest.getIs_liked());
        review.setRating(reviewRequest.getRating());
        review.setComment(reviewRequest.getComment());
    }

    @Transactional
    public Review saveReview(@NotNull @Valid ReviewRequest reviewRequest) {
        Review review = new Review();
        modifyReview(reviewRequest, review);
        return reviewRepository.save(review);
    }

    @Transactional
    public Review updateReview(@NotNull @Valid ReviewRequest reviewRequest, @NotBlank String id) {
        return reviewRepository.findById(UUID.fromString(id)).map(review -> {
            modifyReview(reviewRequest, review);
            return reviewRepository.save(review);
        }).orElseThrow(() -> new ReviewNotFoundException(Review.class, "id", id));
    }

    @Transactional
    public void updateReview(@NotNull @Valid Review review) {
        reviewRepository.save(review);
    }

    @Transactional
    public void deleteReview(@NotBlank String id) {
        if (!reviewRepository.existsById(UUID.fromString(id)))
            throw new ReviewNotFoundException(Review.class, "id", id);

        reviewRepository.deleteById(UUID.fromString(id));
    }
}
