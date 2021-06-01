package com.crumbs.reviewservice.responses;

import com.crumbs.reviewservice.models.Review;
import com.crumbs.reviewservice.models.User;
import com.crumbs.reviewservice.projections.UserClassView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.EntityModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewWithDetails {
    private EntityModel<Review> recipe;
    private UserClassView author;
}
