package com.crumbs.recipeservice.responses;

import com.crumbs.recipeservice.projections.IngredientView;
import com.crumbs.recipeservice.projections.RecipeView;
import com.crumbs.recipeservice.projections.UserClassView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DietResponse {
    private UUID id;
    private String title;
    private String description;
    private Integer duration;
    private boolean isPrivate;
    private LocalDateTime createdAt;
    private UserClassView author;
    private List<RecipeView> recipes;
    private Set<IngredientView> ingredients;
}
