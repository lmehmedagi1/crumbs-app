package com.crumbs.recipeservice.controllers;

import com.crumbs.recipeservice.models.Recipe;
import com.crumbs.recipeservice.requests.RecipeRequest;
import com.crumbs.recipeservice.responses.RecipeResponse;
import com.crumbs.recipeservice.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
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

    @PostMapping("/recipes/create")
    public ResponseEntity<RecipeResponse> createRecipe(@RequestBody @Valid RecipeRequest createRecipeRequest, HttpServletResponse response) {
        try {
            final Recipe recipe = recipeService.saveRecipe(createRecipeRequest);
            return ResponseEntity.ok(new RecipeResponse(recipe));
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PatchMapping("/recipes/update")
    public ResponseEntity<RecipeResponse> updateRecipe(@RequestBody @Valid RecipeRequest updateRecipeRequest, HttpServletResponse response){
        try {
            final Recipe recipe = recipeService.updateRecipe(updateRecipeRequest);
            return ResponseEntity.ok(new RecipeResponse(recipe));
        } catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/recipes/delete")
    public ResponseEntity<RecipeResponse> deleteRecipe(@RequestBody @Valid RecipeRequest deleteRecipeRequest, HttpServletResponse response) {
        try {
            final Recipe recipe = recipeService.deleteRecipe(deleteRecipeRequest);
            return ResponseEntity.ok(new RecipeResponse(recipe));
        } catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
