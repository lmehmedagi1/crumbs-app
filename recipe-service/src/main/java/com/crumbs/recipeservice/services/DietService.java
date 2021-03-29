package com.crumbs.recipeservice.services;

import com.crumbs.recipeservice.exceptions.CategoryNotFoundException;
import com.crumbs.recipeservice.exceptions.DietNotFoundException;
import com.crumbs.recipeservice.models.Diet;
import com.crumbs.recipeservice.repositories.DietRepository;
import com.crumbs.recipeservice.requests.DietRequest;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Diet> getAllDiets() {
        return dietRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Diet getDiet(@NotNull UUID id) {
        return dietRepository.findById(id).orElseThrow(DietNotFoundException::new);
    }

    private void modifyDiet(DietRequest dietRequest, Diet diet) {
        diet.setTitle(dietRequest.getTitle());
        diet.setDescription(dietRequest.getDescription());
        diet.setDuration(dietRequest.getDuration());
        diet.setIsPrivate(dietRequest.getIs_private());
        diet.setRecipes(new ArrayList<>());
    }

    @Transactional
    public Diet saveDiet(@NotNull @Valid DietRequest dietRequest) {
        Diet diet = new Diet();
        modifyDiet(dietRequest, diet);
        dietRepository.save(diet);
        return diet;
    }

    @Transactional
    public Diet updateDiet(@NotNull @Valid DietRequest dietRequest, @NotNull UUID id) {
        return dietRepository.findById(id).map(diet -> {
            modifyDiet(dietRequest, diet);
            return dietRepository.save(diet);
        }).orElseThrow(CategoryNotFoundException::new);
    }

    @Transactional
    public void updateDiet(@NotNull @Valid Diet updatedDiet) {
        dietRepository.save(updatedDiet);
    }

    @Transactional
    public void deleteDiet(@NotNull UUID id) {
        if (!dietRepository.existsById(id))
            throw new DietNotFoundException("The specified diet does not exist :(");

        dietRepository.deleteById(id);
    }
}