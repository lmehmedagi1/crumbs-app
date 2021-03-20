package com.crumbs.reviewservice.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
public class ReviewRequest {
    // UUID must be VERSION 4 !!
    @NotNull
    @NotBlank
    @Pattern(regexp = "^[0-9A-F]{8}-[0-9A-F]{4}-4[0-9A-F]{3}-[89AB][0-9A-F]{3}-[0-9A-F]{12}$",
            flags = Pattern.Flag.CASE_INSENSITIVE, message = "UUID string format is invalid!")
    private String user_id;

    @NotNull
    @NotBlank
    @Pattern(regexp = "^[0-9A-F]{8}-[0-9A-F]{4}-4[0-9A-F]{3}-[89AB][0-9A-F]{3}-[0-9A-F]{12}$",
            flags = Pattern.Flag.CASE_INSENSITIVE, message = "UUID string format is invalid!")
    private String recipe_id;

    @NotNull
    private Boolean is_liked;

    @Min(value = 1, message = "Rating must be between 1-5 inclusive!")
    @Max(value = 5, message = "Rating must be between 1-5 inclusive!")
    private Integer rating;

    @NotBlank
    @Size(max = 30, message = "Comment exceeds allowed limit of 8000 characters!")
    @Pattern(regexp = "^[A-Za-z0-9 _.,!\"'-/]*$", flags = Pattern.Flag.UNICODE_CASE,
            message = "Comment can only contain letters, numbers, spaces, and punctuation!")
    private String comment;
}
