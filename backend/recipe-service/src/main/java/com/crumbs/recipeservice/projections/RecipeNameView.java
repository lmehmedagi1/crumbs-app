package com.crumbs.recipeservice.projections;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeNameView {
    private UUID id;
    private String name;
}
