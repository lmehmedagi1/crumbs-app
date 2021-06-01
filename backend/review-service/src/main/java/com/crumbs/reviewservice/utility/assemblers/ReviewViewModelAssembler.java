package com.crumbs.reviewservice.utility.assemblers;

import com.crumbs.reviewservice.controllers.ReviewController;
import com.crumbs.reviewservice.projections.ReviewView;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ReviewViewModelAssembler implements RepresentationModelAssembler<ReviewView, EntityModel<ReviewView>> {
    @Override
    public EntityModel<ReviewView> toModel(ReviewView reviewView) {
        return EntityModel.of(reviewView, linkTo(methodOn(ReviewController.class).getReviewById(reviewView.getReviewId())).withSelfRel());
    }
}