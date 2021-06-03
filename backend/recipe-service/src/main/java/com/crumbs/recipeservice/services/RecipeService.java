package com.crumbs.recipeservice.services;

import com.crumbs.recipeservice.exceptions.RecipeNotFoundException;
import com.crumbs.recipeservice.exceptions.UnauthorizedException;
import com.crumbs.recipeservice.models.Category;
import com.crumbs.recipeservice.models.Image;
import com.crumbs.recipeservice.models.Ingredient;
import com.crumbs.recipeservice.models.Recipe;
import com.crumbs.recipeservice.projections.RecipeNameView;
import com.crumbs.recipeservice.projections.RecipeView;
import com.crumbs.recipeservice.projections.UserRecipeView;
import com.crumbs.recipeservice.repositories.CategoryRepository;
import com.crumbs.recipeservice.repositories.ImageRepository;
import com.crumbs.recipeservice.repositories.IngredientRepository;
import com.crumbs.recipeservice.repositories.RecipeRepository;
import com.crumbs.recipeservice.requests.OptionRequest;
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
import java.time.LocalDateTime;
import java.util.*;

@Service
@Validated
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final ImageRepository imageRepository;
    private final IngredientRepository ingredientRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, ImageRepository imageRepository,
                         IngredientRepository ingredientRepository, CategoryRepository categoryRepository) {
        this.recipeRepository = recipeRepository;
        this.imageRepository = imageRepository;
        this.ingredientRepository = ingredientRepository;
        this.categoryRepository = categoryRepository;
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
        recipe.setPreparationTime(recipeRequest.getPreparationTime());
        recipe.setCreatedAt(LocalDateTime.now());
        recipe.setAdvice(recipeRequest.getAdvice());

        Set<Ingredient> ingredients = new HashSet<>();
        for (UUID ingredient : recipeRequest.getIngredients())
            ingredients.add(ingredientRepository.findById(ingredient).get());
        recipe.setIngredients(ingredients);

        Set<Category> categories = new HashSet<>();
        for (UUID category : recipeRequest.getCategories())
            categories.add(categoryRepository.findById(category).get());
        recipe.setCategories(categories);

        List<Image> images = new ArrayList<>();
        for (String image : recipeRequest.getImages()) {
            Image newImage = new Image();
            newImage.setImage(image);
            newImage.setRecipe(recipe);
            images.add(newImage);
        }

        if (recipe.getImages() != null) {
            recipe.getImages().clear();
            recipe.getImages().addAll(images);
        } else {
            recipe.setImages(images);
        }
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

    @Transactional(readOnly = true)
    public List<RecipeView> getTopMonthlyRecipePreviews(List<UUID> topMonthly) {
        return recipeRepository.findTopMonthlyRecepies(topMonthly).getContent();
    }

    @Transactional(readOnly = true)
    public List<RecipeView> getTopDailyRecipePreviews(List<UUID> topDaily) {
        return recipeRepository.findTopDailyRecepies(topDaily).getContent();
    }

    @Transactional(readOnly = true)
    public List<UserRecipeView> getUserRecipes(@NotNull UUID id) {
        return recipeRepository.findByUserId(id);
    }

    @Transactional(readOnly = true)
    public UserRecipeView getRecipeView(UUID id) {
        return recipeRepository.findViewById(id);
    }

    @Transactional(readOnly = true)
    public String getRecipeImage(UUID id) {
        Image image = imageRepository.findTop1ByRecipe_Id(id);
        return image != null ? image.getImage() : null;
    }

    @Transactional(readOnly = true)
    public List<RecipeNameView> searchSelectRecipes(OptionRequest optionRequest) {
        return recipeRepository.getSelectSearchRecipes(optionRequest.getSearchTerm(), PageRequest.of(0, 10)).getContent();
    }
}
