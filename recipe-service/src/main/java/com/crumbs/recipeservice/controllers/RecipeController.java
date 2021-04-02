package com.crumbs.recipeservice.controllers;

import com.crumbs.recipeservice.models.Category;
import com.crumbs.recipeservice.models.Recipe;
import com.crumbs.recipeservice.projections.RecipeView;
import com.crumbs.recipeservice.requests.RecipeRequest;
import com.crumbs.recipeservice.services.RecipeService;
import com.crumbs.recipeservice.utility.RecipeModelAssembler;
import com.crumbs.recipeservice.utility.RecipeViewModelAssembler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
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

    @Autowired
    public RecipeController(RecipeService recipeService, RecipeModelAssembler recipeModelAssembler, RecipeViewModelAssembler recipeViewModelAssembler) {
        this.recipeService = recipeService;
        this.recipeModelAssembler = recipeModelAssembler;
        this.recipeViewModelAssembler = recipeViewModelAssembler;
    }

    public CollectionModel<EntityModel<Recipe>> getAllRecipes() {
        List<EntityModel<Recipe>> recipes = recipeService.getAllRecipes()
                .stream()
                .map(recipeModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(recipes, linkTo(methodOn(RecipeController.class).getAllRecipes()).withSelfRel());

    }

//    @GetMapping
//    public CollectionModel<EntityModel<Recipe>> getRecipes(@RequestParam Map<String, String> allRequestParams)
//            throws HttpRequestMethodNotSupportedException {
//        if (allRequestParams != null && !allRequestParams.isEmpty())
//            throw new HttpRequestMethodNotSupportedException("GET");
//
//        return getAllRecipes();
//    }

    @GetMapping
    public CollectionModel<EntityModel<Recipe>> getRecipes(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "25") Integer pageSize,
            @RequestParam(defaultValue = "id") String sort) {

        List<EntityModel<Recipe>> recipes = recipeService.getRecipes(pageNo, pageSize, sort)
                .stream().map(recipeModelAssembler::toModel).collect(Collectors.toList());

        return CollectionModel.of(recipes, linkTo(methodOn(RecipeController.class).getAllRecipes()).withSelfRel());
    }

    @RequestMapping(params = "userId", method = RequestMethod.GET)
    public CollectionModel<EntityModel<Recipe>> getRecipesForUser(@RequestParam("userId") @NotNull UUID userId,
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "2") Integer pageSize,
            @RequestParam(defaultValue = "id") String sort) {

        List<EntityModel<Recipe>> recipes = recipeService.getRecipesForUser(userId, pageNo, pageSize, sort)
                .stream().map(recipeModelAssembler::toModel).collect(Collectors.toList());

        return CollectionModel.of(recipes);
    }

    @RequestMapping(params = "categoryId", method = RequestMethod.GET)
    public CollectionModel<EntityModel<RecipeView>> getRecipesByCategory(@RequestParam("categoryId") @NotNull UUID userId,
                                                                         @RequestParam(defaultValue = "0") Integer pageNo,
                                                                         @RequestParam(defaultValue = "2") Integer pageSize,
                                                                         @RequestParam(defaultValue = "id") String sort) {

        List<EntityModel<RecipeView>> recipes = recipeService.getRecipesByCategoryPreview(userId, pageNo, pageSize, sort)
                .stream().map(recipeViewModelAssembler::toModel).collect(Collectors.toList());

        return CollectionModel.of(recipes);
    }



    @RequestMapping(params = "id", method = RequestMethod.GET)
    public EntityModel<Recipe> getRecipe(@RequestParam("id") @NotNull UUID id) {
        return recipeModelAssembler.toModel(recipeService.getRecipe(id));
    }

    @PostMapping
    public ResponseEntity<?> createRecipe(@RequestBody @Valid RecipeRequest recipeRequest) {
        final Recipe recipe = recipeService.saveRecipe(recipeRequest);
        EntityModel<Recipe> entityModel = recipeModelAssembler.toModel(recipe);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @PatchMapping(consumes = "application/json")
    public ResponseEntity<?> updateRecipe(@RequestParam("id") @NotNull UUID id, @RequestBody @Valid RecipeRequest
            recipeRequest) {
        final Recipe recipe = recipeService.updateRecipe(recipeRequest, id);
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
    public ResponseEntity<?> deleteIngredient(@RequestParam("id") @NotNull UUID id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }
}
