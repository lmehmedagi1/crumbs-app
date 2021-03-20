package com.crumbs.recipeservice.responses;

import com.crumbs.recipeservice.models.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class IngredientResponse {
    @NonNull
    private Ingredient ingredient;
}
