package com.crumbs.recipeservice.projections;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor

public class IngredientView {
    private UUID id;
    private String name;
}
