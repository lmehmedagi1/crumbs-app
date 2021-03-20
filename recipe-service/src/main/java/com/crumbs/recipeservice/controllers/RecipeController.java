package com.crumbs.recipeservice.controllers;

import com.crumbs.recipeservice.models.Recipe;
import com.crumbs.recipeservice.requests.CreateRecipeRequest;
import com.crumbs.recipeservice.requests.UpdateRecipeRequest;
import com.crumbs.recipeservice.responses.RecipeResponse;
import com.crumbs.recipeservice.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @GetMapping("/recipes")
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        return ResponseEntity.ok(recipeService.getRecipes());
    }

    @GetMapping("/recipe")
    public ResponseEntity<Recipe> getRecipe(@RequestParam @Valid String id) {
        return ResponseEntity.ok(recipeService.getRecipe(id));
    }

    @PostMapping("/recipes/create")
    public ResponseEntity<RecipeResponse> createRecipe(@RequestBody @Valid CreateRecipeRequest createRecipeRequest) {
        try {
            final Recipe recipe = recipeService.saveRecipe(createRecipeRequest);
            return ResponseEntity.ok(new RecipeResponse(recipe));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PatchMapping("/recipes/update")
    public ResponseEntity<RecipeResponse> updateRecipe(@RequestBody @Valid UpdateRecipeRequest updateRecipeRequest) {
        final Recipe recipe = recipeService.updateRecipe(updateRecipeRequest);
        return ResponseEntity.ok(new RecipeResponse(recipe));
    }

    @DeleteMapping("/recipes/delete")
    public void deleteIngredient(@RequestParam @Valid String id) {
        recipeService.deleteRecipe(id);
    }
}
