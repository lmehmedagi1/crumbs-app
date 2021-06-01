package com.crumbs.reviewservice.controllers;

//import com.crumbs.reviewservice.amqp.ReviewCreatedEvent;
import com.crumbs.reviewservice.models.Review;
import com.crumbs.reviewservice.models.User;
import com.crumbs.reviewservice.projections.ReviewView;
import com.crumbs.reviewservice.projections.UserClassView;
import com.crumbs.reviewservice.projections.UserRecipeView;
import com.crumbs.reviewservice.requests.ReviewRequest;
import com.crumbs.reviewservice.requests.ReviewWebClientRequest;
import com.crumbs.reviewservice.services.ReviewService;
import com.crumbs.reviewservice.utility.JwtConfigAndUtil;
import com.crumbs.reviewservice.utility.assemblers.ReviewModelAssembler;
import com.crumbs.reviewservice.utility.assemblers.ReviewViewModelAssembler;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
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
    private final ReviewViewModelAssembler reviewViewModelAssembler;
    private final ReviewWebClientRequest reviewWebClientRequest;
//    private final RabbitTemplate rabbitTemplate;

    @Autowired
    ReviewController(ReviewService reviewService, ReviewModelAssembler reviewModelAssembler,
                     ReviewWebClientRequest reviewWebClientRequest, ReviewViewModelAssembler reviewViewModelAssembler/* , RabbitTemplate rabbitTemplate*/) {
        this.reviewService = reviewService;
        this.reviewModelAssembler = reviewModelAssembler;
        this.reviewWebClientRequest = reviewWebClientRequest;
        this.reviewViewModelAssembler = reviewViewModelAssembler;
//        this.rabbitTemplate = rabbitTemplate;
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

    @RequestMapping(value="/comments", params = "recipeId", method = RequestMethod.GET)
    public CollectionModel<EntityModel<ReviewView>> getReviewsOfRecipe(@RequestParam("recipeId") @NotNull UUID recipeId,
                                                                       @RequestParam(defaultValue = "0") Integer pageNo,
                                                                       @RequestParam(defaultValue = "4") Integer pageSize) {

        reviewWebClientRequest.checkIfRecipeExists(recipeId);

        Pageable paging = PageRequest.of(pageNo, pageSize);
        List<ReviewView> reviews = reviewService.getReviewsOfRecipe(recipeId, paging);

        for (ReviewView rV : reviews) {
            UUID authorId = rV.getAuthor().getId();
            UserClassView user = reviewWebClientRequest.getUserPreview(authorId);
            rV.setAuthor(user);
        }

        return CollectionModel.of(reviews.stream().map(reviewViewModelAssembler::toModel).collect(Collectors.toList()),
                linkTo(methodOn(ReviewController.class)
                        .getReviewsOfRecipe(recipeId, pageNo, pageSize)).withSelfRel());

    }

    @RequestMapping(value = "/topMonthly", params = "pageNo", method = RequestMethod.GET)
    public UUID[] getHighestRated(@RequestParam("pageNo") @NotNull int pageNo,
                                  @RequestParam(defaultValue = "4") Integer pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        return reviewService.getHighestRated(paging).toArray(new UUID[0]);
    }

    @RequestMapping(value = "/topDaily", params = "pageNo", method = RequestMethod.GET)
    public UUID[] getHighestRatedDaily(@RequestParam("pageNo") @NotNull int pageNo,
                                       @RequestParam(defaultValue = "3") Integer pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        return reviewService.getHighestRatedDaily(paging).toArray(new UUID[0]);
    }


    @RequestMapping(value = "/rating", params = "recipeId", method = RequestMethod.GET)
    public Double getRecipeRating(@RequestParam("recipeId") @NotNull UUID recipeId) {
        reviewWebClientRequest.checkIfRecipeExists(recipeId);
        return reviewService.getRecipeRating(recipeId);
    }

    @RequestMapping(value = "/review-of-user",params = "entityId", method = RequestMethod.GET)
    public ResponseEntity<?> getReviewOfEntityFromUser(@RequestParam("entityId") @NotNull UUID entityId,
                                                      @RequestHeader("Authorization") String jwt) {

        final UUID userId = getUserIdFromJwt(jwt);
        reviewWebClientRequest.checkIfRecipeExists(entityId);
        reviewWebClientRequest.checkIfUserExists(jwt);

        final Review review = reviewService.getReviewOfEntityFromUser(entityId, userId);
        EntityModel<Review> entityModel = reviewModelAssembler.toModel(review);

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @PostMapping
    public ResponseEntity<?> createReviewOfRecipe(@RequestBody @Valid ReviewRequest reviewRequest, @RequestHeader("Authorization") String jwt) {

        final UUID userId = getUserIdFromJwt(jwt);
        reviewWebClientRequest.checkIfRecipeExists(UUID.fromString(reviewRequest.getEntity_id()));
        reviewWebClientRequest.checkIfUserExists(jwt);

        final Review newReview = reviewService.createReview(reviewRequest, userId);
        EntityModel<Review> entityModel = reviewModelAssembler.toModel(newReview);
//
//        ReviewCreatedEvent createdEvent = new ReviewCreatedEvent(UUID.randomUUID().toString(), newReview.getId(), reviewRequest.getComment());
//        rabbitTemplate.convertAndSend("REVIEW_EXCHANGE", "REVIEW_ROUTING_KEY", createdEvent);

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    private ReviewView getRv(Review newReview) {
        ReviewView reviewView = new ReviewView(newReview.getId(), newReview.getComment(), newReview.getEntityId(),
                newReview.getCreatedAt(), newReview.getLastModify(), newReview.getUserId());
        UUID authorId = newReview.getUserId();
        UserClassView user = reviewWebClientRequest.getUserPreview(authorId);
        reviewView.setAuthor(user);

        return reviewView;
    }

    @PatchMapping(consumes = "application/json")
    public ResponseEntity<?> saveReview(  @RequestParam(defaultValue = "noId") String id,
                                          @RequestBody @Valid ReviewRequest reviewRequest,
                                          @RequestHeader("Authorization") String jwt) {

        UUID userId = getUserIdFromJwt(jwt);
        reviewWebClientRequest.checkIfRecipeExists(UUID.fromString(reviewRequest.getEntity_id()));
        reviewWebClientRequest.checkIfUserExists(jwt);
        Review review;
        if(id.equals("noId") || id.equals("")) {
            review = reviewService.getReviewOfEntityFromUser(UUID.fromString(reviewRequest.getEntity_id()), userId);
            if(review == null) {
                final Review newReview = reviewService.createReview(reviewRequest, userId);
                if(reviewRequest.getComment() != null) {
                    ReviewView reviewView = getRv(newReview);
                    return ResponseEntity.ok(reviewViewModelAssembler.toModel(reviewView));
                } else
                    return ResponseEntity.ok(reviewModelAssembler.toModel(newReview));
            }
            id = review.getId().toString();
        }
        if(reviewRequest.getIs_liked() != null) {
            reviewService.updateReviewLike(reviewRequest.getIs_liked(), UUID.fromString(id));
        } else if (reviewRequest.getComment() != null) {
            reviewService.updateReviewComment(reviewRequest.getComment(), UUID.fromString(id));
        } else {
            reviewService.updateReviewRating(reviewRequest.getRating(), UUID.fromString(id));
        }
        review = reviewService.getReview(UUID.fromString(id));
        if(reviewRequest.getComment() != null) {
            ReviewView reviewView = getRv(review);
            return ResponseEntity.ok(reviewViewModelAssembler.toModel(reviewView));
        } else
            return ResponseEntity.ok(reviewModelAssembler.toModel(review));
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

    @GetMapping("/likes/recipes")
    public List<UserRecipeView> getUserLikedRecipes(@RequestHeader("Authorization") String jwt) {
        UUID userId = getUserIdFromJwt(jwt);
        List<UUID> recipeIds = reviewService.getUserLikedRecipes(userId);
        List<UserRecipeView> recipes = new ArrayList<>();
        for (UUID id : recipeIds) {
            recipes.add(reviewWebClientRequest.getRecipeViewById(id));
        }
        return recipes;
    }

    @GetMapping("/likes/diets")
    public List<UserRecipeView> getUserLikedDiets(@RequestHeader("Authorization") String jwt) {
        UUID userId = getUserIdFromJwt(jwt);
        List<UUID> dietIds = reviewService.getUserLikedDiets(userId);
        List<UserRecipeView> recipes = new ArrayList<>();
        for (UUID id : dietIds) {
            recipes.add(reviewWebClientRequest.getDietViewById(id));
        }
        return recipes;
    }
}
