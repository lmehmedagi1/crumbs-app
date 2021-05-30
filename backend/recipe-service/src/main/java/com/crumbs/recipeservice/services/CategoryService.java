package com.crumbs.recipeservice.services;

import com.crumbs.recipeservice.exceptions.CategoryNotFoundException;
import com.crumbs.recipeservice.models.Category;
import com.crumbs.recipeservice.projections.CategoryView;
import com.crumbs.recipeservice.repositories.CategoryRepository;
import com.crumbs.recipeservice.requests.CategoryRequest;
import com.crumbs.recipeservice.requests.OptionRequest;
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
import java.util.List;
import java.util.UUID;

@Service
@Validated
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<Category> getCategories(Integer pageNo, Integer pageSize, String sort) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sort).ascending());
        Slice<Category> slicedProducts = categoryRepository.findAll(paging);
        return slicedProducts.getContent();
    }

    @Transactional(readOnly = true)
    public Category getCategory(@NotNull UUID id) {
        return categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);
    }

    private void modifyCategory(CategoryRequest categoryRequest, Category category) {
        category.setName(categoryRequest.getName());
    }

    @Transactional
    public Category saveCategory(@NotNull @Valid CategoryRequest categoryRequest) {
        Category category = new Category();
        modifyCategory(categoryRequest, category);
        return categoryRepository.save(category);
    }

    @Transactional
    public Category updateCategory(@NotNull @Valid CategoryRequest categoryRequest, @NotNull UUID id) {
        return categoryRepository.findById(id).map(category -> {
            modifyCategory(categoryRequest, category);
            return categoryRepository.save(category);
        }).orElseThrow(CategoryNotFoundException::new);
    }

    @Transactional
    public void updateCategory(@NotNull @Valid Category updatedCategory) {
        categoryRepository.save(updatedCategory);
    }

    @Transactional
    public void deleteCategory(@NotNull UUID id) {
        if (!categoryRepository.existsById(id))
            throw new CategoryNotFoundException();

        categoryRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<CategoryView> getCategoryByType(@NotNull OptionRequest optionRequest) {
        return categoryRepository.findCategoriesByType(optionRequest.getType(), optionRequest.getSearchTerm());
    }
}
