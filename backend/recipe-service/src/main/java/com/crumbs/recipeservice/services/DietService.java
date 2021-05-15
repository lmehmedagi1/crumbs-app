package com.crumbs.recipeservice.services;

import com.crumbs.recipeservice.exceptions.CategoryNotFoundException;
import com.crumbs.recipeservice.exceptions.DietNotFoundException;
import com.crumbs.recipeservice.exceptions.UnauthorizedException;
import com.crumbs.recipeservice.models.Diet;
import com.crumbs.recipeservice.repositories.DietRepository;
import com.crumbs.recipeservice.requests.DietRequest;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Validated
public class DietService {

    private final DietRepository dietRepository;

    @Autowired
    public DietService(DietRepository dietRepository) {
        this.dietRepository = dietRepository;
    }

    @Transactional(readOnly = true)
    public List<Diet> getDiets(Integer pageNo, Integer pageSize, String sort) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sort).ascending());
        Slice<Diet> slicedProducts = dietRepository.findAll(paging);
        return slicedProducts.getContent();
    }

    @Transactional(readOnly = true)
    public Diet getDiet(@NotNull UUID id) {
        return dietRepository.findById(id).orElseThrow(DietNotFoundException::new);
    }

    private void modifyDiet(DietRequest dietRequest, UUID userId, Diet diet) {
        diet.setTitle(dietRequest.getTitle());
        diet.setDescription(dietRequest.getDescription());
        diet.setDuration(dietRequest.getDuration());
        diet.setIsPrivate(dietRequest.getIs_private());
        diet.setUserId(userId);
        diet.setRecipes(new ArrayList<>());
    }

    @Transactional
    public Diet saveDiet(@NotNull @Valid DietRequest dietRequest, @NotNull UUID userId) {
        Diet diet = new Diet();
        modifyDiet(dietRequest, userId, diet);
        dietRepository.save(diet);
        return diet;
    }

    @Transactional
    public Diet updateDiet(@NotNull @Valid DietRequest dietRequest, @NotNull UUID id, @NotNull UUID userId) {
        Diet diet = dietRepository.findByIdAndUserId(id, userId);
        if (diet == null)
            throw new UnauthorizedException("You don't have permission to update this diet");

        modifyDiet(dietRequest, userId, diet);
        return dietRepository.save(diet);
    }

    @Transactional
    public void updateDiet(@NotNull @Valid Diet updatedDiet) {
        dietRepository.save(updatedDiet);
    }

    @Transactional
    public void deleteDiet(@NotNull UUID id, @NotNull UUID userId) {
        if (dietRepository.findByIdAndUserId(id, userId) == null)
            throw new DietNotFoundException("You don't have permission to delete this diet");

        dietRepository.deleteById(id);
    }
}