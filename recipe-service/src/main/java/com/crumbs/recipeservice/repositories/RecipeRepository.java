package com.crumbs.recipeservice.repositories;

import com.crumbs.recipeservice.models.Recipe;
import com.crumbs.recipeservice.projections.RecipeView;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, UUID> {

    Slice<Recipe> findByUserId(UUID uuid, Pageable pageable);

    //@Query("SELECT r.id as id, r.title as title, r.description as description from Recipe r WHERE ?1 in (SELECT c.id FROM r.categories c)")
    Slice<RecipeView> findByCategories_Id(UUID uuid, Pageable pageable);
}
