package com.crumbs.recipeservice.repositories;

import com.crumbs.recipeservice.models.Recipe;
import com.crumbs.recipeservice.projections.RecipeNameView;
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
            "FROM Recipe r " +
            "INNER JOIN r.categories c " +
            "WHERE (?3 < 1L OR c.id in (?1)) " +
            "AND (?2 IS NULL OR lower(r.title) like lower(concat('%', ?2,'%'))) " +
            "group by r.id, r.title, r.description, r.userId " +
            "having ?3 < 1L OR count(c.id) = ?3 ")
    Slice<RecipeView> findAllPreviews(List<UUID> categories, String title, Long size, Pageable pageable);

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

    @Query("SELECT new com.crumbs.recipeservice.projections.UserRecipeView(r.id, r.title, r.description, 0., '') " +
            "FROM Recipe r WHERE r.userId=?1")
    List<UserRecipeView> findByUserId(UUID id);

    @Query("SELECT new com.crumbs.recipeservice.projections.UserRecipeView(r.id, r.title, r.description, 0., '') " +
            "FROM Recipe r WHERE r.id=?1")
    UserRecipeView findViewById(UUID id);

    @Query("SELECT new com.crumbs.recipeservice.projections.RecipeNameView(r.id, r.title) " +
            "FROM Recipe r WHERE lower(r.title) like lower(concat('%', ?1,'%'))")
    Slice<RecipeNameView> getSelectSearchRecipes(String search, Pageable pageable);
}
