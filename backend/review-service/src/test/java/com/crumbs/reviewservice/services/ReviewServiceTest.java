package com.crumbs.reviewservice.services;


import com.crumbs.reviewservice.exceptions.ReviewNotFoundException;
import com.crumbs.reviewservice.models.Review;
import com.crumbs.reviewservice.requests.ReviewRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    void testGetReviewCorrectId() {
        final Review review = reviewService.getReview(UUID.fromString("9469a486-1e50-4aaf-a760-1daf770a2147"));
        assertAll(
                () -> assertEquals(true, review.getIsLiked()),
                () -> assertEquals(4, review.getRating()),
                () -> assertEquals("Yessir boiii3", review.getComment()));
    }


    @Test
    void testGetDailyRecepies() {
        Pageable paging = PageRequest.of(0, 2);
        List<UUID> list = reviewService.getHighestRated(paging);
        assertAll(() -> assertTrue(list.contains(UUID.fromString("2e0233d2-6e01-455c-8724-2117ad252ced"))));
    }

    @Test
    void testGetMonthlyRecepies() {
        Pageable paging = PageRequest.of(0, 2);
        List<UUID> list = reviewService.getHighestRatedDaily(paging);
        assertAll(() -> assertTrue(list.contains(UUID.fromString("2e0233d2-6e01-455c-8724-2117ad252ced"))));
    }

    @Test
    void testCreateReviewSuccess() {
        final ReviewRequest createRecipeRequest = new ReviewRequest("5ccafc30-b1b3-4f74-ba3c-79583a3129c7", "recipe", true, 3, "Sir Meso");
        final Review review = reviewService.createReview(createRecipeRequest, UUID.fromString("5ccafc30-b1b3-4f74-ba3c-79583a3129c6"));
        assertAll(
                () -> assertEquals(true, review.getIsLiked()),
                () -> assertEquals(3, review.getRating()),
                () -> assertEquals("Sir Meso", review.getComment()));
    }

    @Test
    void testUpdateReviewCommentSuccess() {
        final int rev = reviewService.updateReviewComment("novi kom", UUID.fromString("9469a486-1e50-4aaf-a760-1daf770a2147"));
        final Review review = reviewService.getReview(UUID.fromString("9469a486-1e50-4aaf-a760-1daf770a2147"));
        assertEquals("novi kom", review.getComment());
    }

    @Test
    void testUpdateReviewLikeSuccess() {
        final int rev = reviewService.updateReviewLike(true, UUID.fromString("9469a486-1e50-4aaf-a760-1daf770a2147"));
        final Review review = reviewService.getReview(UUID.fromString("9469a486-1e50-4aaf-a760-1daf770a2147"));
        assertEquals(true, review.getIsLiked());
    }

    @Test
    void testUpdateReviewRatingSuccess() {
        final int rev = reviewService.updateReviewRating(3, UUID.fromString("9469a486-1e50-4aaf-a760-1daf770a2147"));
        final Review review = reviewService.getReview(UUID.fromString("9469a486-1e50-4aaf-a760-1daf770a2147"));
        assertEquals(3, review.getRating());
    }

    @Test
    void testDeleteReviewSuccess() {
        assertThrows(ReviewNotFoundException.class, () -> reviewService.getReview(UUID.fromString("5ccafc30-b1b3-4f74-ba3c-79583a3129c5")));
    }
}
