package com.crumbs.recipeservice.services;

import com.crumbs.recipeservice.exceptions.CategoryNotFoundException;
import com.crumbs.recipeservice.exceptions.RecipeNotFoundException;
import com.crumbs.recipeservice.models.Category;
import com.crumbs.recipeservice.models.Recipe;
import com.crumbs.recipeservice.requests.CreateCategoryRequest;
import com.crumbs.recipeservice.requests.CreateRecipeRequest;
import com.crumbs.recipeservice.requests.UpdateCategoryRequest;
import com.crumbs.recipeservice.requests.UpdateRecipeRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;
    //fb244361-88cb-14eb-8ecd-0242ac130003
    @Test
    void testGetRecipeIncorrectId() {
        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategory("fb244360-88cb-11eb-8dcd-0242ac130005"));
    }

    @Test
    void testGetRecipeNull() {
        assertThrows(NullPointerException.class, () -> categoryService.getCategory(null));
    }

    @Test
    void testGetRecipeCorrectId() {
        final Category category = categoryService.getCategory("fb244361-88cb-14eb-8ecd-0242ac130003");
        assertEquals("Kolac", category.getName());
    }

    @Test
    void testCreateRecipeNullInputParameter() {
        assertThrows(NullPointerException.class, () -> {
            categoryService.saveCategory(null);
        });
    }

    @Test
    void testCreateRecipeSuccess() {
        final CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest("Arslan");
        final Category category = categoryService.saveCategory(createCategoryRequest);
        assertEquals("Arslan", category.getName());
    }

    @Test
    void testUpdateRecipeSuccess() {
        final UpdateCategoryRequest updateCategoryRequest = new UpdateCategoryRequest("fb244361-88cb-14eb-8ecd-0242ac130003", "NoViName");
        final Category category = categoryService.updateCategory(updateCategoryRequest);
        assertEquals("NoViName", category.getName());
    }

    @Test
    void testDeleteRecipeSuccess() {
        categoryService.deleteCategory("fb244361-88cb-14eb-8ecd-0242ac130003");
        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategory("fb244361-88cb-14eb-8ecd-0242ac130003"));
    }
}