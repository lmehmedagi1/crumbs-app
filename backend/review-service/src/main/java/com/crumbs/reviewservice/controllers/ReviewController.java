package com.crumbs.reviewservice.controllers;

import com.crumbs.reviewservice.amqp.ReviewCreatedEvent;
import com.crumbs.reviewservice.models.Review;
import com.crumbs.reviewservice.requests.ReviewRequest;
import com.crumbs.reviewservice.requests.ReviewWebClientRequest;
import com.crumbs.reviewservice.responses.ListWrapper;
import com.crumbs.reviewservice.services.ReviewService;
import com.crumbs.reviewservice.utility.JwtConfigAndUtil;
import com.crumbs.reviewservice.utility.assemblers.ReviewModelAssembler;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
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
    private final ReviewWebClientRequest reviewWebClientRequest;
    //private final RabbitTemplate rabbitTemplate;

    @Autowired
    ReviewController(ReviewService reviewService, ReviewModelAssembler reviewModelAssembler,
                     ReviewWebClientRequest reviewWebClientRequest /*, RabbitTemplate rabbitTemplate*/) {
        this.reviewService = reviewService;
        this.reviewModelAssembler = reviewModelAssembler;
        this.reviewWebClientRequest = reviewWebClientRequest;
        //this.rabbitTemplate = rabbitTemplate;
    }

    @RequestMapping(params = "id", method = RequestMethod.GET)
    public EntityModel<Review> getReviewById(@RequestParam("id") @NotNull UUID id) {
        return reviewModelAssembler.toModel(reviewService.getReview(id));
    }

    @RequestMapping(params = "userId", method = RequestMethod.GET)
    public CollectionModel<EntityModel<Review>> getReviewsOfUser(@RequestParam("userId") @NotNull UUID userId, @RequestHeader("Authorization") String jwt) {

        reviewWebClientRequest.checkIfUserExists(jwt);

        List<EntityModel<Review>> reviews = reviewService.getReviewsOfUser(userId).stream()
                .map(reviewModelAssembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(reviews,
                linkTo(methodOn(ReviewController.class).getReviewsOfUser(userId, jwt)).withSelfRel());
    }

    @RequestMapping(params = "recipeId", method = RequestMethod.GET)
    public CollectionModel<EntityModel<Review>> getReviewsOfRecipe(@RequestParam("recipeId") @NotNull UUID recipeId) {

        reviewWebClientRequest.checkIfRecipeExists(recipeId);

        List<EntityModel<Review>> reviews = reviewService.getReviewsOfRecipe(recipeId).stream()
                .map(reviewModelAssembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(reviews,
                linkTo(methodOn(ReviewController.class).getReviewsOfRecipe(recipeId)).withSelfRel());
    }

    @RequestMapping(value = "/topMonthly", params = "pageNo", method = RequestMethod.GET)
    public UUID[] getHighestRated(@RequestParam("pageNo") @NotNull int pageNo, @RequestHeader("Authorization") String jwt) {
        //reviewWebClientRequest.checkIfUserExists(jwt);
        Pageable paging = PageRequest.of(pageNo, 4);
        UUID[] var = reviewService.getHighestRated(paging).toArray(new UUID[0]);
        return var;
    }

    @RequestMapping(value = "/rating", params = "recipeId", method = RequestMethod.GET)
    public Double getRecipeRating(@RequestParam("recipeId") @NotNull UUID recipeId) {
        reviewWebClientRequest.checkIfRecipeExists(recipeId);
        return reviewService.getRecipeRating(recipeId);
    }

    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody @Valid ReviewRequest reviewRequest, @RequestHeader("Authorization") String jwt) {

        final UUID userId = getUserIdFromJwt(jwt);
        reviewWebClientRequest.checkIfRecipeExists(UUID.fromString(reviewRequest.getRecipe_id()));
        reviewWebClientRequest.checkIfUserExists(jwt);

        final Review newReview = reviewService.createReview(reviewRequest, userId);
        EntityModel<Review> entityModel = reviewModelAssembler.toModel(newReview);

//        ReviewCreatedEvent createdEvent = new ReviewCreatedEvent(UUID.randomUUID().toString(), newReview.getId(), reviewRequest.getComment());
//        rabbitTemplate.convertAndSend("REVIEW_EXCHANGE", "REVIEW_ROUTING_KEY", createdEvent);

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @PatchMapping(consumes = "application/json")
    public ResponseEntity<?> updateReview(@RequestParam("id") @NotNull UUID id,
                                          @RequestBody @Valid ReviewRequest reviewRequest,
                                          @RequestHeader("Authorization") String jwt) {

        UUID userId = getUserIdFromJwt(jwt);
        reviewWebClientRequest.checkIfRecipeExists(UUID.fromString(reviewRequest.getRecipe_id()));
        reviewWebClientRequest.checkIfUserExists(jwt);

        final Review updatedReview = reviewService.updateReview(reviewRequest, id, userId);
        EntityModel<Review> entityModel = reviewModelAssembler.toModel(updatedReview);
        return ResponseEntity.ok(entityModel);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteReview(@RequestParam("id") @NotNull UUID id, @RequestHeader("Authorization") String jwt) {
        UUID userId = getUserIdFromJwt(jwt);
        reviewWebClientRequest.checkIfUserExists(jwt);
        reviewService.deleteReview(id, userId);
        return ResponseEntity.noContent().build();
    }

    private UUID getUserIdFromJwt(String jwt) {
        return UUID.fromString(new JwtConfigAndUtil().extractUserId(jwt.substring(7)));
    }
}
