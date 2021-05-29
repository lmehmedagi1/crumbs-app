package com.crumbs.recipeservice.repositories;

import com.crumbs.recipeservice.models.Recipe;
import com.crumbs.recipeservice.projections.RecipeView;
import com.crumbs.recipeservice.projections.UserRecipeView;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, UUID> {

    @Query("SELECT new com.crumbs.recipeservice.projections.RecipeView(r.id, r.title, r.description, r.userId) " +
            "FROM Recipe r")
    Slice<RecipeView> findAllPreviews(Pageable pageable);

    @Query("SELECT new com.crumbs.recipeservice.projections.RecipeView(r.id, r.title, r.description, r.userId) " +
            "FROM Recipe r WHERE r.userId = ?1")
    Slice<RecipeView> findRecipesForUserId(UUID uuid, Pageable pageable);

    @Query("SELECT new com.crumbs.recipeservice.projections.RecipeView(r.id, r.title, r.description, r.userId) " +
            "FROM Recipe r WHERE ?1 IN (SELECT c.id FROM r.categories c)")
    Slice<RecipeView> findRecipesInCategory(UUID uuid, Pageable pageable);

    @Query("SELECT new com.crumbs.recipeservice.projections.RecipeView(r.id, r.title, r.description, r.userId) " +
            "FROM Recipe r WHERE r.id IN (?1)")
    Slice<RecipeView> findTopMonthlyRecepies(List<UUID> uuids);

    @Query("SELECT new com.crumbs.recipeservice.projections.RecipeView(r.id, r.title, r.description, r.userId) " +
            "FROM Recipe r WHERE r.id IN (?1)")
    Slice<RecipeView> findTopDailyRecepies(List<UUID> uuids);

    Recipe findByIdAndUserId(UUID id, UUID userId);

    @Query("SELECT new com.crumbs.recipeservice.projections.UserRecipeView(r.id, r.title, r.description, 0.) " +
            "FROM Recipe r WHERE r.userId=?1")
    List<UserRecipeView> findByUserId(UUID id);

    @Query("SELECT new com.crumbs.recipeservice.projections.UserRecipeView(r.id, r.title, r.description, 0.) " +
            "FROM Recipe r WHERE r.id=?1")
    UserRecipeView findViewById(UUID id);
}
