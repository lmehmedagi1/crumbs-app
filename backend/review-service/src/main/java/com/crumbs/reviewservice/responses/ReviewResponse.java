package com.crumbs.reviewservice.responses;

import com.crumbs.reviewservice.models.Review;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class ReviewResponse {
    @NotNull
    private final Review review;
}
