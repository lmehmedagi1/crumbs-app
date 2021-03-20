package com.crumbs.reviewservice.response;

import com.crumbs.reviewservice.model.Review;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class ReviewResponse {
    @NotNull
    private final Review review;
}
