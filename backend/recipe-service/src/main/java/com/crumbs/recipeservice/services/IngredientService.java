package com.crumbs.recipeservice.services;

import com.crumbs.recipeservice.exceptions.IngredientNotFoundException;
import com.crumbs.recipeservice.models.Ingredient;
import com.crumbs.recipeservice.projections.IngredientView;
import com.crumbs.recipeservice.repositories.IngredientRepository;
import com.crumbs.recipeservice.requests.IngredientRequest;
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
import java.util.Optional;
import java.util.UUID;

@Service
@Validated
public class IngredientService {
    @Autowired
    private IngredientRepository ingredientRepository;

    @Transactional(readOnly = true)
    public List<Ingredient> getIngredients(Integer pageNo, Integer pageSize, String sort) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sort).ascending());
        Slice<Ingredient> slicedProducts = ingredientRepository.findAll(paging);
        return slicedProducts.getContent();
    }

    @Transactional(readOnly = true)
    public Ingredient getIngredient(@NotNull UUID id) {
        final Optional<Ingredient> optionalIngredient = ingredientRepository.findById(id);

        if (optionalIngredient.isEmpty())
            throw new IngredientNotFoundException();

        return optionalIngredient.get();
    }

    @Transactional
    public Ingredient saveIngredient(@NotNull @Valid IngredientRequest ingredientRequest) {
        Ingredient recipe = new Ingredient();
        recipe.setName(ingredientRequest.getName());
        ingredientRepository.save(recipe);
        return recipe;
    }

    @Transactional
    public Ingredient updateIngredient(@NotNull @Valid IngredientRequest ingredientRequest, @NotNull UUID id) {
        Optional<Ingredient> optional = ingredientRepository.findById(id);
        if (optional.isPresent()) {
            Ingredient recipe = optional.get();
            recipe.setName(ingredientRequest.getName());
            ingredientRepository.save(recipe);
            return recipe;
        } else {
            throw new IngredientNotFoundException();
        }
    }

    @Transactional
    public void updateIngredient(@NotNull @Valid Ingredient updatedIngredient) {
        ingredientRepository.save(updatedIngredient);
    }

    @Transactional
    public void deleteIngredient(@NotNull UUID id) {
        if (!ingredientRepository.existsById(id))
            throw new IngredientNotFoundException();

        ingredientRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<IngredientView> searchIngredients(@NotNull OptionRequest optionRequest) {
        return ingredientRepository.searchIngredients(optionRequest.getSearchTerm());
    }
}
