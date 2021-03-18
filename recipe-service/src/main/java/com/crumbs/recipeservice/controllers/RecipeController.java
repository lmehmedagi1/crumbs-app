package com.crumbs.recipeservice.controllers;

import com.crumbs.recipeservice.exceptions.RecipeNotFoundException;
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

    @GetMapping("/recipe")
    public ResponseEntity<Recipe> getRecipe(@RequestParam @Valid String id) {
        try {
            return ResponseEntity.ok(recipeService.getRecipe(id));
        } catch (RecipeNotFoundException recipeNotFoundException){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, recipeNotFoundException.getMessage());
        }
    }

    @PostMapping("/recipes/create")
    public ResponseEntity<RecipeResponse> createRecipe(@RequestBody @Valid CreateRecipeRequest createRecipeRequest) {
        try {
            final Recipe recipe = recipeService.saveRecipe(createRecipeRequest);
            return ResponseEntity.ok(new RecipeResponse(recipe));
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PatchMapping("/recipes/update")
    public ResponseEntity<RecipeResponse> updateRecipe(@RequestBody @Valid UpdateRecipeRequest updateRecipeRequest){
        try {
            final Recipe recipe = recipeService.updateRecipe(updateRecipeRequest);
            return ResponseEntity.ok(new RecipeResponse(recipe));
        } catch(RecipeNotFoundException recipeNotFoundException){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, recipeNotFoundException.getMessage());
        }
    }

    @DeleteMapping("/recipes/delete")
    public void deleteRecipe(@RequestParam @Valid String id) {
        try {
            recipeService.deleteRecipe(id);
        } catch(RecipeNotFoundException recipeNotFoundException){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, recipeNotFoundException.getMessage());
        }
    }
}
