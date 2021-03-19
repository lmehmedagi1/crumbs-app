package com.crumbs.reviewservice.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class UpdateReviewRequest {
    @NonNull
    @NotBlank
    private String id;

    @NonNull
    @NotBlank
    private String userId;

    @NonNull
    @NotBlank
    private String recipeId;

    @NonNull
    private Boolean isLiked;

    private Integer rating;

    @NotBlank
    private String comment;
}
