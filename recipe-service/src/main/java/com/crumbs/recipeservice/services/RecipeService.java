package com.crumbs.recipeservice.services;

import com.crumbs.recipeservice.models.Recipe;
import com.crumbs.recipeservice.repositories.RecipeRepository;
import com.crumbs.recipeservice.requests.RecipeRequest;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Transactional(readOnly = true)
    public List<Recipe> getRecipes() {
        return recipeRepository.findAll();
    }

    @Transactional
    public Recipe saveRecipe(@NonNull @Valid RecipeRequest createRecipeRequest) {
        return new Recipe();
    }

    public Recipe updateRecipe(RecipeRequest updateRecipeRequest) {
        return new Recipe();
    }

    public Recipe deleteRecipe(RecipeRequest deleteRecipeRequest) {
        return null;
    }
}
