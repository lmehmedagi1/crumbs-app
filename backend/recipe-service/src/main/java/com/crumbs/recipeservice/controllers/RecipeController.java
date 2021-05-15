package com.crumbs.recipeservice.controllers;

import com.crumbs.recipeservice.models.Recipe;
import com.crumbs.recipeservice.models.User;
import com.crumbs.recipeservice.projections.RecipeView;
import com.crumbs.recipeservice.projections.UserView;
import com.crumbs.recipeservice.requests.RecipeRequest;
import com.crumbs.recipeservice.requests.WebClientRequest;
import com.crumbs.recipeservice.responses.RecipeWithDetails;
import com.crumbs.recipeservice.services.RecipeService;
import com.crumbs.recipeservice.utility.JwtConfigAndUtil;
import com.crumbs.recipeservice.utility.assemblers.RecipeModelAssembler;
import com.crumbs.recipeservice.utility.assemblers.RecipeViewModelAssembler;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/recipes")
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Internal Server Error")
})
public class RecipeController {

    private final RecipeService recipeService;
    private final RecipeModelAssembler recipeModelAssembler;
    private final RecipeViewModelAssembler recipeViewModelAssembler;
    private final WebClientRequest webClientRequest;

    @Autowired
    public RecipeController(RecipeService recipeService, RecipeModelAssembler recipeModelAssembler,
                            RecipeViewModelAssembler recipeViewModelAssembler, WebClientRequest webClientRequest) {
        this.recipeService = recipeService;
        this.recipeModelAssembler = recipeModelAssembler;
        this.recipeViewModelAssembler = recipeViewModelAssembler;
        this.webClientRequest = webClientRequest;
    }

    @GetMapping
    public CollectionModel<EntityModel<RecipeView>> getRecipePreviews(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "title") String sort,
            @RequestHeader("Authorization") String jwt) {

        List<RecipeView> recipes = recipeService.getRecipePreviews(pageNo, pageSize, sort);
        for (RecipeView recipe : recipes) {
            UUID authorId = recipe.getAuthor().getUserId();
            User user = webClientRequest.checkIfUserExists(jwt);
            recipe.setAuthor(new UserView(user.getId(), user.getUsername(), user.getUserProfile().getAvatar()));
        }

        return CollectionModel.of(recipes.stream().map(recipeViewModelAssembler::toModel).collect(Collectors.toList()),
                linkTo(methodOn(RecipeController.class)
                        .getRecipePreviews(pageNo, pageSize, sort, jwt)).withSelfRel());
    }

    @RequestMapping(params = "userId", method = RequestMethod.GET)
    public CollectionModel<EntityModel<RecipeView>> getRecipePreviewsForUser(
            @RequestParam("userId") @NotNull UUID userId,
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "title") String sort,
            @RequestHeader("Authorization") String jwt) {

        List<RecipeView> recipes = recipeService.getRecipePreviewsForUser(userId, pageNo, pageSize, sort);
        for (RecipeView recipe : recipes) {
            UUID authorId = recipe.getAuthor().getUserId();
            User user = webClientRequest.checkIfUserExists(jwt);
            recipe.setAuthor(new UserView(user.getId(), user.getUsername(), user.getUserProfile().getAvatar()));
        }

        return CollectionModel.of(recipes.stream().map(recipeViewModelAssembler::toModel).collect(Collectors.toList()),
                linkTo(methodOn(RecipeController.class)
                        .getRecipePreviewsForUser(userId, pageNo, pageSize, sort, jwt)).withSelfRel());
    }

    @RequestMapping(params = "categoryId", method = RequestMethod.GET)
    public CollectionModel<EntityModel<RecipeView>> getRecipePreviewsForCategory(
            @RequestParam("categoryId") @NotNull UUID categoryId,
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "title") String sort,
            @RequestHeader("Authorization") String jwt) {

        List<RecipeView> recipes = recipeService.getRecipePreviewsForCategory(categoryId, pageNo, pageSize, sort);
        for (RecipeView recipe : recipes) {
            UUID authorId = recipe.getAuthor().getUserId();
            User user = webClientRequest.checkIfUserExists(jwt);
            recipe.setAuthor(new UserView(user.getId(), user.getUsername(), user.getUserProfile().getAvatar()));
        }

        return CollectionModel.of(recipes.stream().map(recipeViewModelAssembler::toModel).collect(Collectors.toList()),
                linkTo(methodOn(RecipeController.class)
                        .getRecipePreviewsForCategory(categoryId, pageNo, pageSize, sort, jwt)).withSelfRel());
    }

    @RequestMapping(params = "id", method = RequestMethod.GET)
    public EntityModel<Recipe> getRecipe(@RequestParam("id") @NotNull UUID id) {
        return recipeModelAssembler.toModel(recipeService.getRecipe(id));
    }

    @RequestMapping(params = {"id", "details"}, method = RequestMethod.GET)
    public EntityModel<?> getRecipe(@RequestParam("id") @NotNull UUID id,
                                    @RequestParam(value = "details", defaultValue = "false") @NotNull Boolean details) {
        if (!details)
            return getRecipe(id);
        else {
            Recipe recipe = recipeService.getRecipe(id);
            User author = webClientRequest.getRecipeAuthor(recipe.getUserId());
            return EntityModel.of(new RecipeWithDetails(recipeModelAssembler.toModel(recipe), author,
                    webClientRequest.getRecipeRating(recipe.getId())));
        }
    }

    @PostMapping
    public ResponseEntity<?> createRecipe(@RequestBody @Valid RecipeRequest recipeRequest, @RequestHeader("Authorization") String jwt) {

        webClientRequest.checkIfUserExists(jwt);
        UUID userId = JwtConfigAndUtil.getUserIdFromJwt(jwt);

        final Recipe recipe = recipeService.saveRecipe(recipeRequest, userId);
        EntityModel<Recipe> entityModel = recipeModelAssembler.toModel(recipe);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @PatchMapping(consumes = "application/json")
    public ResponseEntity<?> updateRecipe(@RequestParam("id") @NotNull UUID id,
                                          @RequestBody @Valid RecipeRequest recipeRequest,
                                          @RequestHeader("Authorization") String jwt) {

        webClientRequest.checkIfUserExists(jwt);
        UUID userId = JwtConfigAndUtil.getUserIdFromJwt(jwt);

        final Recipe recipe = recipeService.updateRecipe(recipeRequest, id, userId);
        EntityModel<Recipe> entityModel = recipeModelAssembler.toModel(recipe);
        return ResponseEntity.ok(entityModel);
    }

    /**
     * PATCH method with partial update, based on JSON Patch
     */
    @PatchMapping(consumes = "application/json-patch+json")
    public ResponseEntity<?> patchRecipe(@RequestParam("id") @NotNull UUID id, @RequestBody JsonPatch patch) {
        try {
            Recipe recipe = recipeService.getRecipe(id);
            final ObjectMapper objectMapper = new ObjectMapper();
            JsonNode patched = patch.apply(objectMapper.convertValue(recipe, JsonNode.class));
            Recipe recipePatched = objectMapper.treeToValue(patched, Recipe.class);
            recipeService.updateRecipe(recipePatched);
            return ResponseEntity.ok(recipeModelAssembler.toModel(recipePatched));
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteIngredient(@RequestParam("id") @NotNull UUID id, @RequestHeader("Authorization") String jwt) {
        UUID userId = JwtConfigAndUtil.getUserIdFromJwt(jwt);
        recipeService.deleteRecipe(id, userId);
        return ResponseEntity.noContent().build();
    }
}
