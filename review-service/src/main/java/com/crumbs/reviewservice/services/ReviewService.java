package com.crumbs.reviewservice.services;

import com.crumbs.reviewservice.exceptions.ReviewNotFoundException;
import com.crumbs.reviewservice.model.Review;
import com.crumbs.reviewservice.repositories.ReviewRepository;
import com.crumbs.reviewservice.requests.ReviewRequest;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;
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
    public Review getReview(String id) throws ReviewNotFoundException {
        return reviewRepository.findById(UUID.fromString(id)).orElseThrow(() ->
                new ReviewNotFoundException("The specified review does not exist :("));
    }

    private void modifyReview(ReviewRequest reviewRequest, Review review) {
        review.setUserId(UUID.fromString(reviewRequest.getUserId()));
        review.setRecipeId(UUID.fromString(reviewRequest.getRecipeId()));
        review.setIsLiked(reviewRequest.getIsLiked());
        review.setRating(reviewRequest.getRating());
        review.setComment(reviewRequest.getComment());
    }

    @Transactional
    public Review saveReview(@NonNull @Valid ReviewRequest reviewRequest) {
        Review review = new Review();
        modifyReview(reviewRequest, review);
        return reviewRepository.save(review);
    }

    @Transactional
    public Review updateReview(@NonNull @Valid ReviewRequest reviewRequest, @NonNull String id) {
        return reviewRepository.findById(UUID.fromString(id)).map(review -> {
            modifyReview(reviewRequest, review);
            return reviewRepository.save(review);
        }).orElseThrow(() -> new ReviewNotFoundException("The specified review does not exist :("));
    }

    @Transactional
    public void deleteReview(@NonNull String id) {
        if (!reviewRepository.existsById(UUID.fromString(id)))
            throw new ReviewNotFoundException("The specified review does not exist :(");

        reviewRepository.deleteById(UUID.fromString(id));
    }
}
