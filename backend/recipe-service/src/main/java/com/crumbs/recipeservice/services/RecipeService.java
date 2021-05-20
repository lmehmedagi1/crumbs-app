package com.crumbs.recipeservice.services;

import com.crumbs.recipeservice.exceptions.DietNotFoundException;
import com.crumbs.recipeservice.exceptions.RecipeNotFoundException;
import com.crumbs.recipeservice.exceptions.UnauthorizedException;
import com.crumbs.recipeservice.models.Diet;
import com.crumbs.recipeservice.models.Recipe;
import com.crumbs.recipeservice.projections.RecipeView;
import com.crumbs.recipeservice.repositories.RecipeRepository;
import com.crumbs.recipeservice.requests.RecipeRequest;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
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
    public List<RecipeView> getRecipePreviews(Integer pageNo, Integer pageSize, String sort) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sort).ascending());
        Slice<RecipeView> slicedProducts = recipeRepository.findAllPreviews(paging);
        return slicedProducts.getContent();
    }

    public List<RecipeView> getRecipePreviewsForUser(UUID userId, Integer pageNo, Integer pageSize, String sort) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sort).ascending());
        Slice<RecipeView> slicedProducts = recipeRepository.findRecipesForUserId(userId, paging);
        return slicedProducts.getContent();
    }

    public List<RecipeView> getRecipePreviewsForCategory(UUID userId, Integer pageNo, Integer pageSize, String sort) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sort).ascending());
        Slice<RecipeView> slicedProducts = recipeRepository.findRecipesInCategory(userId, paging);
        return slicedProducts.getContent();
    }

    @Transactional(readOnly = true)
    public Recipe getRecipe(@NotNull UUID id) {
        return recipeRepository.findById(id).orElseThrow(RecipeNotFoundException::new);
    }

    private void modifyRecipe(RecipeRequest recipeRequest, UUID userId, Recipe recipe) {
        recipe.setTitle(recipeRequest.getTitle());
        recipe.setDescription(recipeRequest.getDescription());
        recipe.setMethod(recipeRequest.getMethod());
        recipe.setUserId(userId);
    }

    @Transactional
    public Recipe saveRecipe(@NonNull @Valid RecipeRequest recipeRequest, @NotNull UUID userId) {
        Recipe recipe = new Recipe();
        modifyRecipe(recipeRequest, userId, recipe);
        return recipeRepository.save(recipe);
    }

    @Transactional
    public Recipe updateRecipe(@NotNull @Valid RecipeRequest recipeRequest, @NotNull UUID id, @NotNull UUID userId) {
        Recipe recipe = recipeRepository.findByIdAndUserId(id, userId);
        if (recipe == null)
            throw new UnauthorizedException("You don't have permission to update this recipe");

        modifyRecipe(recipeRequest, userId, recipe);
        return recipeRepository.save(recipe);
    }

    @Transactional
    public void updateRecipe(@NotNull @Valid Recipe updatedRecipe) {
        recipeRepository.save(updatedRecipe);
    }

    @Transactional
    public void deleteRecipe(@NonNull UUID id, @NotNull UUID userId) {
        if (recipeRepository.findByIdAndUserId(id, userId) == null)
            throw new RecipeNotFoundException("You don't have permission to delete this recipe");

        recipeRepository.deleteById(id);
    }

    public List<RecipeView> getTopMonthlyRecipePreviews(List<UUID> topMonthly) {
        return recipeRepository.findTopMonthlyRecepies(topMonthly).getContent();
    }
}
