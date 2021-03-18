package com.crumbs.recipeservice.services;

import com.crumbs.recipeservice.models.Recipe;
import com.crumbs.recipeservice.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Transactional(readOnly = true)
    public List<Recipe> getRecipes() {
        return recipeRepository.findAll();
    }
}
