package com.crumbs.reviewservice.controllers;

import com.crumbs.reviewservice.models.Review;
import com.crumbs.reviewservice.requests.ReviewRequest;
import com.crumbs.reviewservice.services.ReviewService;
import com.crumbs.reviewservice.utility.ReviewModelAssembler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/reviews")
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Internal Server Error")
})
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewModelAssembler reviewModelAssembler;

    @Autowired
    ReviewController(ReviewService reviewService, ReviewModelAssembler reviewModelAssembler) {
        this.reviewService = reviewService;
        this.reviewModelAssembler = reviewModelAssembler;
    }

    public CollectionModel<EntityModel<Review>> getAllReviews() {
        List<EntityModel<Review>> reviews = reviewService.getAllReviews().stream()
                .map(reviewModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(reviews, linkTo(methodOn(ReviewController.class).getAllReviews()).withSelfRel());
    }

    @GetMapping
    public CollectionModel<EntityModel<Review>> getReviews(@RequestParam @Nullable Map<String, String> allRequestParams)
            throws HttpRequestMethodNotSupportedException {
        if (allRequestParams != null && !allRequestParams.isEmpty())
            throw new HttpRequestMethodNotSupportedException("GET");

        return getAllReviews();
    }

    @RequestMapping(params = "id", method = RequestMethod.GET)
    public EntityModel<Review> getReviewById(@RequestParam("id") @NotNull UUID id) {
        return reviewModelAssembler.toModel(reviewService.getReview(id));
    }

    @RequestMapping(params = "userId", method = RequestMethod.GET)
    public CollectionModel<EntityModel<Review>> getReviewsOfUser(@RequestParam("userId") @NotNull UUID userId) {
        List<EntityModel<Review>> reviews = reviewService.getReviewsOfUser(userId).stream()
                .map(reviewModelAssembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(reviews, linkTo(methodOn(ReviewController.class).getAllReviews()).withSelfRel());
    }

    @RequestMapping(params = "recipeId", method = RequestMethod.GET)
    public CollectionModel<EntityModel<Review>> getReviewsOfRecipe(@RequestParam("recipeId") @NotNull UUID recipeId) {
        List<EntityModel<Review>> reviews = reviewService.getReviewsOfRecipe(recipeId).stream()
                .map(reviewModelAssembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(reviews, linkTo(methodOn(ReviewController.class).getAllReviews()).withSelfRel());
    }

    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody @Valid ReviewRequest reviewRequest) {
        final Review newReview = reviewService.saveReview(reviewRequest);
        EntityModel<Review> entityModel = reviewModelAssembler.toModel(newReview);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @PatchMapping(consumes = "application/json")
    public ResponseEntity<?> updateReview(@RequestParam("id") @NotNull UUID id, @RequestBody @Valid ReviewRequest reviewRequest) {
        final Review updatedReview = reviewService.updateReview(reviewRequest, id);
        EntityModel<Review> entityModel = reviewModelAssembler.toModel(updatedReview);
        return ResponseEntity.ok(entityModel);
    }

    /**
     * PATCH method with partial update, based on JSON Patch
     */
    @PatchMapping(consumes = "application/json-patch+json")
    public ResponseEntity<?> patchReview(@RequestParam("id") @NotNull UUID id, @RequestBody JsonPatch patch) {
        try {
            Review review = reviewService.getReview(id);
            final ObjectMapper objectMapper = new ObjectMapper();
            JsonNode patched = patch.apply(objectMapper.convertValue(review, JsonNode.class));
            Review reviewPatched = objectMapper.treeToValue(patched, Review.class);
            reviewService.updateReview(reviewPatched);
            return ResponseEntity.ok(reviewPatched);
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteReview(@RequestParam("id") @NotNull UUID id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}