package com.crumbs.reviewservice.repositories;

import com.crumbs.reviewservice.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
    List<Review> findByUserId(UUID uuid);

    List<Review> findByRecipeId(UUID uuid);

    @Query("SELECT ROUND(AVG(r.rating), 2) FROM Review r WHERE r.recipeId = ?1")
    Double getAvgRatingOfRecipe(UUID uuid);
}
