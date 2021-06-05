package com.crumbs.reviewservice.projections;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewView {
    UUID reviewId;
    String comment;
    UUID entityId;
    LocalDateTime createdAt;
    LocalDateTime modifiedAt;
    UserClassView author;
    Double recipeRating;
    Integer rating;

    public ReviewView(UUID reviewId, String comment, UUID entityId, LocalDateTime createdAt, LocalDateTime modifiedAt, UUID authorId) {
        this.reviewId = reviewId;
        this.comment = comment;
        this.entityId = entityId;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.author = new UserClassView(authorId);
    }
}
