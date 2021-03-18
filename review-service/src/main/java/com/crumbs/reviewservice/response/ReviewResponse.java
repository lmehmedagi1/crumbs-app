package com.crumbs.reviewservice.response;

import com.crumbs.reviewservice.model.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class ReviewResponse {
    @NonNull
    private final Review review;
}
