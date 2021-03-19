package com.crumbs.reviewservice.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
public class ReviewRequest {

    @NonNull
    @NotBlank
    private String userId;

    @NonNull
    @NotBlank
    private String recipeId;

    @NonNull
    private Boolean isLiked;

    @Min(1)
    @Max(5)
    private Integer rating;

    @NotBlank
    @Size(min = 1, max = 30, message = "Comment exceeds allowed limit of 8000 characters!")
    @Pattern(regexp = "^[A-Za-z0-9 _.,!\"'-/]*$", flags = Pattern.Flag.UNICODE_CASE, message = "Comment can only contain letters, numbers, spaces, and punctuation.")
    private String comment;
}
