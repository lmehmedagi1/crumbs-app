package com.crumbs.recipeservice.services;

import com.crumbs.recipeservice.exceptions.IngredientNotFoundException;
import com.crumbs.recipeservice.models.Ingredient;
import com.crumbs.recipeservice.repositories.IngredientRepository;
import com.crumbs.recipeservice.requests.IngredientRequest;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class IngredientService {
    @Autowired
    private IngredientRepository ingredientRepository;

    @Transactional(readOnly = true)
    public List<Ingredient> getIngredients() {
        return ingredientRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Ingredient getIngredient(String id) {
        final Optional<Ingredient> optionalIngredient = ingredientRepository.findById(UUID.fromString(id));

        if (optionalIngredient.isEmpty())
            throw new IngredientNotFoundException(id);

        return optionalIngredient.get();
    }

    @Transactional
    public Ingredient createIngredient(@NonNull @Valid IngredientRequest ingredientRequest) {
        Ingredient recipe = new Ingredient();
        recipe.setName(ingredientRequest.getName());
        ingredientRepository.save(recipe);
        return recipe;
    }

    @Transactional
    public Ingredient updateIngredient(@NonNull @Valid IngredientRequest ingredientRequest, @NonNull String id) {
        Optional<Ingredient> optional = ingredientRepository.findById(UUID.fromString(id));
        if (optional.isPresent()) {
            Ingredient recipe = optional.get();
            recipe.setName(ingredientRequest.getName());
            ingredientRepository.save(recipe);
            return recipe;
        } else {
            throw new IngredientNotFoundException(id);
        }
    }

    @Transactional
    public void deleteIngredient(@NonNull @Valid String id) {
        if (!ingredientRepository.existsById(UUID.fromString(id)))
            throw new IngredientNotFoundException(id);

        ingredientRepository.deleteById(UUID.fromString(id));
    }
}
