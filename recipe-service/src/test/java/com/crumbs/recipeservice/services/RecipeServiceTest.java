package com.crumbs.recipeservice.services;

import com.crumbs.recipeservice.exceptions.RecipeNotFoundException;
import com.crumbs.recipeservice.models.Recipe;
import com.crumbs.recipeservice.requests.RecipeRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RecipeServiceTest {
/*
    @Autowired
    private RecipeService recipeService;

    //fb244360-88cb-11eb-8dcd-0242ac130003
    @Test
    void testGetRecipeIncorrectId() {
        assertThrows(RecipeNotFoundException.class, () -> recipeService.getRecipe(UUID.fromString("fb244360-88cb-11eb-8dcd-0242ac130005")));
    }

    @Test
    void testGetRecipeNull() {
        assertThrows(ConstraintViolationException.class, () -> recipeService.getRecipe(null));
    }

    @Test
    void testGetRecipeCorrectId() {
        final Recipe recipe = recipeService.getRecipe(UUID.fromString("fb244360-88cb-11eb-8dcd-0242ac130003"));
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
        String method = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s.";
        final RecipeRequest recipeRequest = new RecipeRequest("Lazanje", "Sir i Meso", method);
        final Recipe recipe = recipeService.saveRecipe(recipeRequest);
        assertAll(
                () -> assertEquals("Lazanje", recipe.getTitle()),
                () -> assertEquals(method, recipe.getMethod()),
                () -> assertEquals("Sir i Meso", recipe.getDescription()));
    }

    @Test
    void testCreateRecipeShortMethod() {
        final RecipeRequest recipeRequest = new RecipeRequest("Lazanje", "Sir i Meso", "MMMMM");
        assertThrows(ConstraintViolationException.class, () -> recipeService.saveRecipe(recipeRequest));
    }


    @Test
    void testUpdateRecipeSuccess() {
        String method = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s.";
        final RecipeRequest recipeRequest = new RecipeRequest("Lazanje nakon apdejta", "Sir", method);
        final Recipe recipe = recipeService.updateRecipe(recipeRequest, UUID.fromString("fb244360-88cb-11eb-8dcd-0242ac130003"));
        assertAll(
                () -> assertEquals("Lazanje nakon apdejta", recipe.getTitle()),
                () -> assertEquals(method, recipe.getMethod()),
                () -> assertEquals("Sir", recipe.getDescription()));
    }

    @Test
    void testDeleteRecipeSuccess() {
        recipeService.deleteRecipe(UUID.fromString("fb244360-88cb-11eb-8dcd-0242ac130003"));
        assertThrows(RecipeNotFoundException.class, () -> recipeService.getRecipe(UUID.fromString("fb244360-88cb-11eb-8dcd-0242ac130003")));
    }
    */
}