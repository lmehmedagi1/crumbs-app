package com.crumbs.reviewservice.services;

import com.crumbs.reviewservice.exceptions.ReviewNotFoundException;
import com.crumbs.reviewservice.exceptions.UnauthorizedException;
import com.crumbs.reviewservice.models.Review;
import com.crumbs.reviewservice.projections.ReviewView;
import com.crumbs.reviewservice.repositories.ReviewRepository;
import com.crumbs.reviewservice.requests.ReviewRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Log4j2
@Service
@Validated
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ApplicationEventPublisher publisher;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, ApplicationEventPublisher publisher) {
        this.reviewRepository = reviewRepository;
        this.publisher = publisher;
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
    public List<ReviewView> getReviewsOfRecipe(@NotNull UUID recipeId, Pageable p) {
        return reviewRepository.findEntityId(recipeId, p);
    }

    @Transactional(readOnly = true)
    public Double getRecipeRating(@NotNull UUID recipeId) {
        Double rating = reviewRepository.getAvgRatingOfRecipe(recipeId);
        rating = rating==null ? 0 : rating;
        return (double) Math.round(rating * 100) / 100;
    }

    @Transactional(readOnly = true)
    public List<UUID> getHighestRated(Pageable p) {
        return reviewRepository.getFourTopRatedForMonth(LocalDateTime.now().minusMonths(1), p);
    }

    @Transactional(readOnly = true)
    public List<UUID> getHighestRatedDaily(Pageable p) {
        return reviewRepository.getTopRatedDaily(LocalDateTime.now().minusDays(1), p);
    }

    @Transactional
    public Review createReview(@NotNull @Valid ReviewRequest reviewRequest, @NotNull UUID userId) {
        Review review = new Review();
        modifyReview(reviewRequest, userId, review);
        log.debug("Saving a review {}", review);
        Review returnReview = reviewRepository.save(review);
        System.out.println("Rev id: " + returnReview.getId() + " a old rev id: " + review.getId());
        return returnReview;
    }

    @Transactional
    public void deleteReview(@NotNull UUID reviewId, @NotNull UUID userId) {
        if (reviewRepository.findByIdAndUserId(reviewId, userId) == null)
            throw new UnauthorizedException("You don't have permission to delete this review");
        reviewRepository.deleteForId(reviewId);
    }

    private void modifyReview(ReviewRequest reviewRequest, UUID userId, Review review) {
        review.setUserId(userId);
        review.setEntityId(UUID.fromString(reviewRequest.getEntity_id()));
        review.setEntityType(Review.EntityType.valueOf(reviewRequest.getEntity_type()));
        review.setIsLiked(reviewRequest.getIs_liked());
        review.setRating(reviewRequest.getRating());
        review.setComment(reviewRequest.getComment());
        review.setCreatedAt(LocalDateTime.now(ZoneId.of("CET")));
    }

    @Transactional(readOnly = true)
    public List<UUID> getUserLikedRecipes(UUID userId) {
        return reviewRepository.getUserLikedRecipes(userId);
    }

    @Transactional(readOnly = true)
    public List<UUID> getUserLikedDiets(UUID userId) {
        return new ArrayList<>();
    }

    public Review getReviewOfEntityFromUser(UUID entityId, UUID userId) {
        return reviewRepository.findByEntityIdAndUserId(entityId, userId);
    }

    public int updateReviewLike(Boolean is_liked, UUID id) {
        return reviewRepository.setLikedForReview(is_liked, id);
    }

    public int updateReviewComment(String comment, UUID id) {
        return reviewRepository.setCommentForReview(comment, id);
    }

    public int updateReviewRating(Integer rating, UUID id) {
        return reviewRepository.setRatingForReview(rating, id);
    }
}
