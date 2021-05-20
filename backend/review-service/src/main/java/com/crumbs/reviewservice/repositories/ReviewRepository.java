package com.crumbs.reviewservice.repositories;

import com.crumbs.reviewservice.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
    List<Review> findByUserId(UUID uuid);

    List<Review> findByRecipeId(UUID uuid);

    @Query(value = "SELECT AVG(r.rating), 2 FROM Review r WHERE r.recipeId = ?1")
    Double getAvgRatingOfRecipe(UUID uuid);

    @Query(value = "SELECT r.recipeId as ids FROM Review r group by r.recipeId order by AVG(r.rating) desc")
    List<UUID> getFourTopRatedForMonth(Pageable pageable);

    Review findByIdAndUserId(UUID id, UUID userId);
}
