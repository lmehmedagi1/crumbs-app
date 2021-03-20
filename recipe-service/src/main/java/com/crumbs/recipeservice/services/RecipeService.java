package com.crumbs.recipeservice.services;

import com.crumbs.recipeservice.exceptions.RecipeNotFoundException;
import com.crumbs.recipeservice.models.Recipe;
import com.crumbs.recipeservice.repositories.RecipeRepository;
import com.crumbs.recipeservice.requests.CreateRecipeRequest;
import com.crumbs.recipeservice.requests.UpdateRecipeRequest;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Transactional(readOnly = true)
    public List<Recipe> getRecipes() {
        return recipeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Recipe getRecipe(String id) {
        final Optional<Recipe> optionalRecipe = recipeRepository.findById(UUID.fromString(id));

        if (optionalRecipe.isEmpty())
            throw new RecipeNotFoundException("The specified recipe does not exist :(");

        return optionalRecipe.get();
    }

    @Transactional
    public Recipe saveRecipe(@NonNull @Valid CreateRecipeRequest createRecipeRequest) {
        Recipe recipe = new Recipe();
        recipe.setTitle(createRecipeRequest.getTitle());
        recipe.setDescription(createRecipeRequest.getDescription());
        recipe.setMethod(createRecipeRequest.getMethod());
        recipeRepository.save(recipe);
        return recipe;
    }

    @Transactional
    public Recipe updateRecipe(@NonNull @Valid UpdateRecipeRequest updateRecipeRequest) {
        Optional<Recipe> optional = recipeRepository.findById(UUID.fromString(updateRecipeRequest.getId()));
        if (optional.isPresent()) {
            Recipe recipe = optional.get();
            recipe.setTitle(updateRecipeRequest.getTitle());
            recipe.setDescription(updateRecipeRequest.getDescription());
            recipe.setMethod(updateRecipeRequest.getMethod());
            recipeRepository.save(recipe);
            return recipe;
        } else {
            throw new RecipeNotFoundException("The specified recipe does not exist :(");
        }
    }

    @Transactional
    public void deleteRecipe(@NonNull @Valid String id) {
        if (!recipeRepository.existsById(UUID.fromString(id)))
            throw new RecipeNotFoundException("The specified recipe does not exist :(");

        recipeRepository.deleteById(UUID.fromString(id));
    }
}
