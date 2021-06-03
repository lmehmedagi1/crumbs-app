package com.crumbs.reviewservice.repositories;

import com.crumbs.reviewservice.models.Review;
import com.crumbs.reviewservice.projections.ReviewView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
    List<Review> findByUserId(UUID uuid);

    @Query("SELECT new com.crumbs.reviewservice.projections.ReviewView(r.id, r.comment, r.entityId, r.createdAt, r.lastModify, r.userId) " +
                    "FROM Review r WHERE r.entityId = ?1")
    List<ReviewView> findEntityId(UUID uuid, Pageable pageable);

    @Query(value = "SELECT AVG(r.rating), 2 FROM Review r WHERE r.entityId = ?1")
    Double getAvgRatingOfRecipe(UUID uuid);

    @Query(value = "SELECT r.entityId as ids FROM Review r where r.createdAt > ?1 group by r.entityId order by AVG(r.rating) desc")
    List<UUID> getFourTopRatedForMonth(LocalDateTime localDateTime, Pageable pageable);

    @Query(value = "SELECT r.entityId as ids FROM Review r where r.createdAt > ?1 group by r.entityId order by AVG(r.rating) desc")
    List<UUID> getTopRatedDaily(LocalDateTime localDateTime, Pageable pageable);

    Review findByIdAndUserId(UUID id, UUID userId);

    @Query("SELECT r.entityId FROM Review r WHERE r.userId = ?1 AND r.isLiked = true")
    List<UUID> getUserLikedRecipes(UUID userId);

    Review findByEntityIdAndUserId(UUID eid, UUID uid);

    @Modifying(clearAutomatically=true)
    @Transactional
    @Query("update Review r set r.isLiked = ?1, r.lastModify = current_timestamp where r.id = ?2")
    int setLikedForReview(Boolean isLiked, UUID id);

    @Modifying(clearAutomatically=true)
    @Transactional
    @Query("update Review r set r.comment = ?1, r.lastModify = current_timestamp where r.id = ?2")
    int setCommentForReview(String comment, UUID id);

    @Modifying(clearAutomatically=true)
    @Transactional
    @Query("update Review r set r.rating = ?1, r.lastModify = current_timestamp where r.id = ?2")
    int setRatingForReview(int rating, UUID id);

    boolean existsReviewByUserIdAndEntityId(UUID userid, UUID entityid);

    @Modifying(clearAutomatically=true)
    @Transactional
    @Query("update Review r set r.comment = null, r.lastModify = current_timestamp where r.id = ?1")
    void deleteForId(UUID reviewId);
}
