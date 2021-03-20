package com.crumbs.reviewservice.controllers;

import com.crumbs.reviewservice.exceptions.ReviewNotFoundException;
import com.crumbs.reviewservice.model.Review;
import com.crumbs.reviewservice.requests.ReviewRequest;
import com.crumbs.reviewservice.services.ReviewService;
import com.crumbs.reviewservice.utility.ReviewModelAssembler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ReviewController {

    private final ObjectMapper objectMapper = new ObjectMapper();
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
    public EntityModel<Review> getReview(@PathVariable String id) throws ReviewNotFoundException {
        return reviewModelAssembler.toModel(reviewService.getReview(id));
    }

    @PostMapping("/reviews")
    public ResponseEntity<?> createReview(@RequestBody @Valid ReviewRequest reviewRequest) throws ReviewNotFoundException {
        final Review newReview = reviewService.saveReview(reviewRequest);
        EntityModel<Review> entityModel = reviewModelAssembler.toModel(newReview);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @PatchMapping(path = "/reviews/{id}", consumes = "application/json")
    public ResponseEntity<?> updateReview(@RequestBody @Valid ReviewRequest reviewRequest, @PathVariable String id) throws ReviewNotFoundException {
        final Review updatedReview = reviewService.updateReview(reviewRequest, id);
        EntityModel<Review> entityModel = reviewModelAssembler.toModel(updatedReview);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    private Review applyPatchToReview(
            JsonPatch patch, Review targetCustomer) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(targetCustomer, JsonNode.class));
        return objectMapper.treeToValue(patched, Review.class);
    }

    /**
     * PATCH method with partial update, based on JSON Patch
     */
    @PatchMapping(path = "/reviews/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<?> patchReview(@PathVariable String id, @RequestBody JsonPatch patch) {
        try {
            Review review = reviewService.getReview(id);
            Review reviewPatched = applyPatchToReview(patch, review);
            reviewService.updateReview(reviewPatched);
            return ResponseEntity.ok(reviewPatched);
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (ReviewNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable String id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}