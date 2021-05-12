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
        final Review review = reviewService.getReview(UUID.fromString("9469a486-1e50-4aaf-a760-1daf770a2147"));
        assertAll(
                () -> assertEquals(true, review.getIsLiked()),
                () -> assertEquals(4, review.getRating()),
                () -> assertEquals("Dobar pravo", review.getComment()));
    }

    @Test
    void testCreateReviewNullInputParameter() {
        assertThrows(NullPointerException.class, () -> {
            reviewService.saveReview(null, null);
        });
    }

    @Test
    void testCreateReviewSuccess() {
        final ReviewRequest createRecipeRequest = new ReviewRequest("5ccafc30-b1b3-4f74-ba3c-79583a3129c7", true, 3, "Sir Meso");
        final Review review = reviewService.saveReview(createRecipeRequest, UUID.fromString("5ccafc30-b1b3-4f74-ba3c-79583a3129c6"));
        assertAll(
                () -> assertEquals(true, review.getIsLiked()),
                () -> assertEquals(3, review.getRating()),
                () -> assertEquals("Sir Meso", review.getComment()));
    }

//    @Test
//    void testUpdateReviewSuccess() {
//        final ReviewRequest updateRecipeRequest = new ReviewRequest("ac8ff8ff-7193-4c45-90bd-9c662cc0494a", true, 3, "Sir Meso");
//        final Review review = reviewService.updateReview(updateRecipeRequest, UUID.fromString("3e8ec94c-3edf-49e0-b548-425088881f60"));
//        assertAll(
//                () -> assertEquals(true, review.getIsLiked()),
//                () -> assertEquals(3, review.getRating()),
//                () -> assertEquals("Sir Meso", review.getComment()));
//    }

    @Test
    void testDeleteReviewSuccess() {
        assertThrows(ReviewNotFoundException.class, () -> reviewService.getReview(UUID.fromString("5ccafc30-b1b3-4f74-ba3c-79583a3129c5")));
    }
}
