package com.crumbs.recipeservice.services;

import com.crumbs.recipeservice.exceptions.CategoryNotFoundException;
import com.crumbs.recipeservice.exceptions.DietNotFoundException;
import com.crumbs.recipeservice.models.Category;
import com.crumbs.recipeservice.models.Diet;
import com.crumbs.recipeservice.repositories.CategoryRepository;
import com.crumbs.recipeservice.requests.CreateCategoryRequest;
import com.crumbs.recipeservice.requests.UpdateCategoryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<Category> getAllCategories( ) {
        return categoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Category getCategory(String id) {
        final Optional<Category> optionalCategory = categoryRepository.findById(UUID.fromString(id));

        if (optionalCategory.isEmpty())
            throw new CategoryNotFoundException("The specified category does not exist :(");

        return optionalCategory.get();
    }

    @Transactional
    public Category saveCategory(CreateCategoryRequest createCategoryRequest) {
        Category category = new Category();
        category.setName(createCategoryRequest.getName());
        categoryRepository.save(category);
        return category;
    }

    @Transactional
    public Category updateCategory(UpdateCategoryRequest updateCategoryRequest) {
        Optional<Category> optional = categoryRepository.findById(UUID.fromString(updateCategoryRequest.getId()));
        if (optional.isPresent()) {
            Category category = optional.get();
            category.setName(updateCategoryRequest.getName());
            categoryRepository.save(category);
            return category;
        } else {
            throw new CategoryNotFoundException("The specified category does not exist :(");
        }
    }

    @Transactional
    public void deleteCategory(String id) {
        if (!categoryRepository.existsById(UUID.fromString(id)))
            throw new CategoryNotFoundException("The specified category does not exist :(");

        categoryRepository.deleteById(UUID.fromString(id));
    }
}
