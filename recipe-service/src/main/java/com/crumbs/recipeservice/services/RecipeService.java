package com.crumbs.recipeservice.services;

import com.crumbs.recipeservice.exceptions.RecipeNotFoundException;
import com.crumbs.recipeservice.models.Recipe;
import com.crumbs.recipeservice.repositories.RecipeRepository;
import com.crumbs.recipeservice.requests.RecipeRequest;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Service
@Validated
public class RecipeService {

    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Transactional(readOnly = true)
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Recipe getRecipe(@NotNull UUID id) {
        return recipeRepository.findById(id).orElseThrow(RecipeNotFoundException::new);
    }

    private void modifyRecipe(RecipeRequest recipeRequest, Recipe recipe) {
        recipe.setTitle(recipeRequest.getTitle());
        recipe.setDescription(recipeRequest.getDescription());
        recipe.setMethod(recipeRequest.getMethod());
    }

    @Transactional
    public Recipe saveRecipe(@NonNull @Valid RecipeRequest recipeRequest) {
        Recipe recipe = new Recipe();
        modifyRecipe(recipeRequest, recipe);
        return recipeRepository.save(recipe);
    }

    @Transactional
    public Recipe updateRecipe(@NotNull @Valid RecipeRequest recipeRequest, @NotNull UUID id) {
        return recipeRepository.findById(id).map(recipe -> {
            modifyRecipe(recipeRequest, recipe);
            return recipeRepository.save(recipe);
        }).orElseThrow(RecipeNotFoundException::new);
    }

    @Transactional
    public void updateRecipe(@NotNull @Valid Recipe updatedRecipe) {
        recipeRepository.save(updatedRecipe);
    }

    @Transactional
    public void deleteRecipe(@NonNull UUID id) {
        if (!recipeRepository.existsById(id))
            throw new RecipeNotFoundException();

        recipeRepository.deleteById(id);
    }
}
