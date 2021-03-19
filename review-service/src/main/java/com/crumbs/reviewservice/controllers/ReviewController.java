package com.crumbs.reviewservice.controllers;

import com.crumbs.reviewservice.exceptions.ReviewNotFoundException;
import com.crumbs.reviewservice.model.Review;
import com.crumbs.reviewservice.requests.ReviewRequest;
import com.crumbs.reviewservice.services.ReviewService;
import com.crumbs.reviewservice.utility.ReviewModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewModelAssembler reviewModelAssembler;

    @Autowired
    ReviewController(ReviewService reviewService, ReviewModelAssembler reviewModelAssembler) {
        this.reviewService = reviewService;
        this.reviewModelAssembler = reviewModelAssembler;
    }

    @GetMapping("/reviews")
    public CollectionModel<EntityModel<Review>> getAllReviews() {
        List<EntityModel<Review>> reviews = reviewService.getAllReviews().stream()
                .map(reviewModelAssembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(reviews, linkTo(methodOn(ReviewController.class).getAllReviews()).withSelfRel());
    }

    @GetMapping("/reviews/{id}")
    public EntityModel<Review> getReview(@PathVariable String id) {
        try {
            return reviewModelAssembler.toModel(reviewService.getReview(id));
        } catch (ReviewNotFoundException reviewNotFoundException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, reviewNotFoundException.getMessage());
        }
    }

    @PostMapping("/reviews")
    public ResponseEntity<?> createReview(@RequestBody @Valid ReviewRequest reviewRequest) {
        try {
            final Review newReview = reviewService.saveReview(reviewRequest);
            EntityModel<Review> entityModel = reviewModelAssembler.toModel(newReview);
            return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        } catch (ReviewNotFoundException reviewNotFoundException) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, reviewNotFoundException.getMessage());
        }
    }

    @PatchMapping("/reviews/{id}")
    public ResponseEntity<?> updateReview(@RequestBody @Valid ReviewRequest reviewRequest, @PathVariable String id) {
        try {
            final Review updatedReview = reviewService.updateReview(reviewRequest, id);
            EntityModel<Review> entityModel = reviewModelAssembler.toModel(updatedReview);
            return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        } catch (ReviewNotFoundException reviewNotFoundException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, reviewNotFoundException.getMessage());
        }
    }

    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable String id) {
        try {
            reviewService.deleteReview(id);
            return ResponseEntity.noContent().build();
        } catch (ReviewNotFoundException reviewNotFoundException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, reviewNotFoundException.getMessage());
        }
    }
}