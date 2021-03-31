package com.crumbs.recipeservice.services;

import com.crumbs.recipeservice.exceptions.CategoryNotFoundException;
import com.crumbs.recipeservice.models.Category;
import com.crumbs.recipeservice.requests.CategoryRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    void testGetRecipeIncorrectId() {
        assertThrows(CategoryNotFoundException.class,
                () -> categoryService.getCategory(UUID.fromString("fb244360-88cb-11eb-8dcd-0242ac130005")));
    }

    @Test
    void testGetRecipeNull() {
        assertThrows(ConstraintViolationException.class, () -> categoryService.getCategory(null));
    }

    @Test
    void testGetRecipeCorrectId() {
        final Category category = categoryService.getCategory(UUID.fromString("fb244361-88cb-14eb-8ecd-0242ac130003"));
        assertEquals("Kolac", category.getName());
    }

    @Test
    void testCreateRecipeNullInputParameter() {
        assertThrows(ConstraintViolationException.class, () -> {
            categoryService.saveCategory(null);
        });
    }

    @Test
    void testCreateRecipeSuccess() {
        final CategoryRequest createCategoryRequest = new CategoryRequest("Arslan");
        final Category category = categoryService.saveCategory(createCategoryRequest);
        assertEquals("Arslan", category.getName());
    }

    @Test
    void testUpdateRecipeSuccess() {
        final CategoryRequest categoryRequest = new CategoryRequest("NoViName");
        final Category category = categoryService.updateCategory(categoryRequest, UUID.fromString("fb244361-88cb-14eb-8ecd-0242ac130003"));
        assertEquals("NoViName", category.getName());
    }

    @Test
    void testDeleteRecipeSuccess() {
        categoryService.deleteCategory(UUID.fromString("fb244361-88cb-14eb-8ecd-0242ac130003"));
        assertThrows(CategoryNotFoundException.class,
                () -> categoryService.getCategory(UUID.fromString("fb244361-88cb-14eb-8ecd-0242ac130003")));
    }
}