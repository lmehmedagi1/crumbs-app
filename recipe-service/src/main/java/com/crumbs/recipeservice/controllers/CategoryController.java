package com.crumbs.recipeservice.controllers;

import com.crumbs.recipeservice.exceptions.CategoryNotFoundException;
import com.crumbs.recipeservice.models.Category;
import com.crumbs.recipeservice.requests.CreateCategoryRequest;
import com.crumbs.recipeservice.requests.UpdateCategoryRequest;
import com.crumbs.recipeservice.responses.CategoryResponse;
import com.crumbs.recipeservice.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/category")
    public ResponseEntity<Category> getCategory(@RequestParam @Valid String id) {
        try {
            return ResponseEntity.ok(categoryService.getCategory(id));
        } catch (CategoryNotFoundException categoryNotFoundException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, categoryNotFoundException.getMessage());
        }
    }

    @PostMapping("/category/create")
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody @Valid String categoryName) {
        try {
            final Category category = categoryService.saveCategory(categoryName);
            return ResponseEntity.ok(new CategoryResponse(category));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PatchMapping("/category/update")
    public ResponseEntity<CategoryResponse> updateCategory(@RequestBody @Valid UpdateCategoryRequest updateCategoryRequest) {
        try {
            final Category category = categoryService.updateCategory(updateCategoryRequest);
            return ResponseEntity.ok(new CategoryResponse(category));
        } catch (CategoryNotFoundException categoryNotFoundException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, categoryNotFoundException.getMessage());
        }
    }

    @DeleteMapping("/category/delete")
    public void deleteCategory(@RequestParam @Valid String id) {
        try {
            categoryService.deleteCategory(id);
        } catch(CategoryNotFoundException categoryNotFoundException){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, categoryNotFoundException.getMessage());
        }
    }

}
