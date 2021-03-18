package com.crumbs.recipeservice.repositories;

import com.crumbs.recipeservice.models.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RecipeRepository extends JpaRepository<Recipe, UUID> {
}
