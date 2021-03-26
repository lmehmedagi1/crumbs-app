package com.crumbs.reviewservice.services;


import com.crumbs.reviewservice.exceptions.ReviewNotFoundException;
import com.crumbs.reviewservice.models.Review;
import com.crumbs.reviewservice.requests.ReviewRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ReviewServiceTest {

    @Autowired
    private ReviewService reviewService;

    @Test
    void testGetReviewIncorrectId() {
        assertThrows(ReviewNotFoundException.class, () -> reviewService.getReview(UUID.fromString("5ccafc30-b1b3-4f74-ba3c-79583a3129c8")));
    }

    @Test
    void testGetReviewNull() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> reviewService.getReview(null));
    }

    @Test
    void testGetReviewCorrectId() {
        final Review review = reviewService.getReview(UUID.fromString("5ccafc30-b1b3-4f74-ba3c-79583a3129c5"));
        assertAll(
                () -> assertEquals(true, review.getIsLiked()),
                () -> assertEquals(4, review.getRating()),
                () -> assertEquals("Dobar pravo", review.getComment()));
    }

    @Test
    void testCreateReviewNullInputParameter() {
        assertThrows(NullPointerException.class, () -> {
            reviewService.saveReview(null);
        });
    }

    @Test
    void testCreateReviewSuccess() {
        final ReviewRequest createRecipeRequest = new ReviewRequest("5ccafc30-b1b3-4f74-ba3c-79583a3129c6", "5ccafc30-b1b3-4f74-ba3c-79583a3129c7", true, 3, "Sir Meso");
        final Review review = reviewService.saveReview(createRecipeRequest);
        assertAll(
                () -> assertEquals(true, review.getIsLiked()),
                () -> assertEquals(3, review.getRating()),
                () -> assertEquals("Sir Meso", review.getComment()));
    }

    @Test
    void testUpdateReviewSuccess() {
        final ReviewRequest updateRecipeRequest = new ReviewRequest("5ccafc30-b1b3-4f74-ba3c-79583a3129c6", "5ccafc30-b1b3-4f74-ba3c-79583a3129c7", true, 3, "Sir Meso");
        final Review review = reviewService.updateReview(updateRecipeRequest, UUID.fromString("5ccafc30-b1b3-4f74-ba3c-79583a3129c5"));
        assertAll(
                () -> assertEquals(true, review.getIsLiked()),
                () -> assertEquals(3, review.getRating()),
                () -> assertEquals("Sir Meso", review.getComment()));
    }

    @Test
    void testDeleteReviewSuccess() {
        reviewService.deleteReview(UUID.fromString("5ccafc30-b1b3-4f74-ba3c-79583a3129c5"));
        assertThrows(ReviewNotFoundException.class, () -> reviewService.getReview(UUID.fromString("5ccafc30-b1b3-4f74-ba3c-79583a3129c5")));
    }
}