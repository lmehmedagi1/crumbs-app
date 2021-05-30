package com.crumbs.recipeservice.repositories;

import com.crumbs.recipeservice.models.Ingredient;
import com.crumbs.recipeservice.projections.CategoryView;
import com.crumbs.recipeservice.projections.IngredientView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, UUID> {

    @Query("SELECT new com.crumbs.recipeservice.projections.IngredientView(i.id, i.name) " +
            "FROM Ingredient i " +
            "WHERE ?1 IS NULL OR ?1 = '''' OR i.name LIKE %?1%")
    List<IngredientView> searchIngredients(String search);

}
