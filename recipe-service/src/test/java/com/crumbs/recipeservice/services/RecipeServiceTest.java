package com.crumbs.recipeservice.services;

import com.crumbs.recipeservice.exceptions.RecipeNotFoundException;
import com.crumbs.recipeservice.models.Recipe;
import com.crumbs.recipeservice.requests.CreateRecipeRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RecipeServiceTest {

    @Autowired
    private RecipeService recipeService;

    @Test
    void testGetRecipeIncorrectId() {
        assertThrows(RecipeNotFoundException.class, () -> recipeService.getRecipe("fb244360-88cb-11eb-8dcd-0242ac130005"));
    }

    @Test
    void testGetRecipeNull() {
        assertThrows(NullPointerException.class, () -> recipeService.getRecipe(null));
    }

    @Test
    void testGetRecipeCorrectId() {
        final Recipe recipe = recipeService.getRecipe("fb244360-88cb-11eb-8dcd-0242ac130003");
        assertAll(
                () -> assertEquals("Šampita", recipe.getTitle()),
                () -> assertEquals("Najgori kolac u istoriji", recipe.getDescription()),
                () -> assertEquals("1. Kupite sampitu; 2. Bacite je", recipe.getMethod()));
    }

    @Test
    void testCreateRecipeNullInputParameter() {
        assertThrows(NullPointerException.class, () -> {
            recipeService.saveRecipe(null);
        });
    }

    @Test
    void testCreateRecipeSuccess() {
        final CreateRecipeRequest createRecipeRequest = new CreateRecipeRequest("Lazanje", "Sir + Meso", "MMMMM");
        final Recipe recipe = recipeService.saveRecipe(createRecipeRequest);
        assertAll(
                () -> assertEquals("Lazanje", recipe.getTitle()),
                () -> assertEquals("MMMMM", recipe.getDescription()),
                () -> assertEquals("Sir + Meso", recipe.getMethod()));
    }

    @Test
    void testUpdateRecipeSuccess() {
        final UpdateRecipeRequest updateRecipeRequest = new UpdateRecipeRequest("fb244360-88cb-11eb-8dcd-0242ac130003", "Lazanje nakon apdejta", "Sir", "MMMMM");
        final Recipe recipe = recipeService.updateRecipe(updateRecipeRequest);
        assertAll(
                () -> assertEquals("Lazanje nakon apdejta", recipe.getTitle()),
                () -> assertEquals("MMMMM", recipe.getDescription()),
                () -> assertEquals("Sir", recipe.getMethod()));
    }

    @Test
    void testDeleteRecipeSuccess() {
        recipeService.deleteRecipe("fb244360-88cb-11eb-8dcd-0242ac130003");
        assertThrows(RecipeNotFoundException.class, () -> recipeService.getRecipe("fb244360-88cb-11eb-8dcd-0242ac130003"));
    }
}