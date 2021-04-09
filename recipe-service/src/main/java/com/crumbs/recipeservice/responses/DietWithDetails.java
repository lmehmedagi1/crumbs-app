package com.crumbs.recipeservice.responses;

import com.crumbs.recipeservice.projections.RecipeView;
import com.crumbs.recipeservice.projections.UserView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DietWithDetails {
    private String title;
    private String description;
    private Integer duration;
    private UserView author;
    private List<RecipeView> recipes;

    public DietWithDetails(String title, String description, Integer duration) {
        this.title = title;
        this.description = description;
        this.duration = duration;
    }
}
