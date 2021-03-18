package com.crumbs.reviewservice.services;

import com.crumbs.reviewservice.exceptions.ReviewNotFoundException;
import com.crumbs.reviewservice.model.Review;
import com.crumbs.reviewservice.repositories.ReviewRepository;
import com.crumbs.reviewservice.requests.CreateReviewRequest;
import com.crumbs.reviewservice.requests.UpdateReviewRequest;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Review getReview(String id) {
        final Optional<Review> optionalReview = reviewRepository.findById(UUID.fromString(id));

        if (optionalReview.isEmpty())
            throw new ReviewNotFoundException("The specified review does not exist :(");

        return optionalReview.get();
    }

    @Transactional
    public Review saveReview(@NonNull @Valid CreateReviewRequest createReviewRequest) {
        Review review = new Review();
        review.setUserId(UUID.fromString(createReviewRequest.getUserId()));
        review.setRecipeId(UUID.fromString(createReviewRequest.getRecipeId()));
        review.setIsLiked(createReviewRequest.getIsLiked());
        review.setRating(createReviewRequest.getRating());
        review.setComment(createReviewRequest.getComment());
        reviewRepository.save(review);
        return review;
    }

    @Transactional
    public Review updateReview(@NonNull @Valid UpdateReviewRequest updateReviewRequest) {
        Optional<Review> optionalReview = reviewRepository.findById(UUID.fromString(updateReviewRequest.getId()));
        if (optionalReview.isPresent()) {
            Review review = optionalReview.get();
            review.setUserId(UUID.fromString(updateReviewRequest.getUserId()));
            review.setRecipeId(UUID.fromString(updateReviewRequest.getRecipeId()));
            review.setIsLiked(updateReviewRequest.getIsLiked());
            review.setRating(updateReviewRequest.getRating());
            review.setComment(updateReviewRequest.getComment());
            reviewRepository.save(review);
            return review;
        } else {
            throw new ReviewNotFoundException("The specified review does not exist :(");
        }
    }

    @Transactional
    public void deleteReview(@NonNull @Valid String id) {
        if (!reviewRepository.existsById(UUID.fromString(id)))
            throw new ReviewNotFoundException("The specified review does not exist :(");

        reviewRepository.deleteById(UUID.fromString(id));
    }
}
