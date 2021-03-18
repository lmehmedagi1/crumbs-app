package com.crumbs.recipeservice.responses;

import com.crumbs.recipeservice.models.Diet;
import com.crumbs.recipeservice.models.Recipe;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class DietResponse {
    @NonNull
    private Diet diet;
}
