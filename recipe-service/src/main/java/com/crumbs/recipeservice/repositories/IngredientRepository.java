package com.crumbs.recipeservice.repositories;

import com.crumbs.recipeservice.models.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IngredientRepository extends JpaRepository<Ingredient, UUID> {
}
