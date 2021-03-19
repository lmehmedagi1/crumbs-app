package com.crumbs.recipeservice.responses;

import com.crumbs.recipeservice.models.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class CategoryResponse {
    @NonNull
    private Category category;
}
