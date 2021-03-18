package com.crumbs.recipeservice.services;

import com.crumbs.recipeservice.exceptions.DietNotFoundException;
import com.crumbs.recipeservice.exceptions.RecipeNotFoundException;
import com.crumbs.recipeservice.models.Diet;
import com.crumbs.recipeservice.models.Recipe;
import com.crumbs.recipeservice.repositories.DietRepository;
import com.crumbs.recipeservice.requests.CreateDietRequest;
import com.crumbs.recipeservice.requests.CreateRecipeRequest;
import com.crumbs.recipeservice.requests.UpdateDietRequest;
import com.crumbs.recipeservice.requests.UpdateRecipeRequest;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DietService {

    @Autowired
    private DietRepository dietRepository;

    @Transactional(readOnly = true)
    public List<Diet> getAllDiets() {
        return dietRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Diet getDiet(String id) {
        final Optional<Diet> optionalDiet = dietRepository.findById(UUID.fromString(id));

        if (optionalDiet.isEmpty())
            throw new DietNotFoundException("The specified diet does not exist :(");

        return optionalDiet.get();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Diet saveDiet(@NonNull @Valid CreateDietRequest createDietRequest) {
        Diet diet = new Diet();
        diet.setTitle(createDietRequest.getTitle());
        diet.setDescription(createDietRequest.getDescription());
        diet.setDuration(createDietRequest.getDuration());
        diet.setIsPrivate(createDietRequest.getIsPrivate());
        diet.setRecipes(new ArrayList<>());
        dietRepository.save(diet);
        return diet;
    }

    @Transactional
    public Diet updateDiet(@NonNull @Valid UpdateDietRequest updateDietRequest) {
        Optional<Diet> optional = dietRepository.findById(UUID.fromString(updateDietRequest.getId()));
        if (optional.isPresent()) {
            Diet diet = optional.get();
            diet.setTitle(updateDietRequest.getTitle());
            diet.setDescription(updateDietRequest.getDescription());
            diet.setDuration(updateDietRequest.getDuration());
            diet.setIsPrivate(updateDietRequest.getIsPrivate());
            dietRepository.save(diet);
            return diet;
        } else {
            throw new DietNotFoundException("The specified diet does not exist :(");
        }
    }

    @Transactional
    public void deleteDiet(@NonNull @Valid String id) {
        if (!dietRepository.existsById(UUID.fromString(id)))
            throw new DietNotFoundException("The specified diet does not exist :(");

        dietRepository.deleteById(UUID.fromString(id));
    }
}
