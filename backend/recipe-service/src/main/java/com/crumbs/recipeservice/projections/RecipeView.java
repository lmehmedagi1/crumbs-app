package com.crumbs.recipeservice.projections;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeView {
    private UUID recipeId;
    private String title;
    private String description;
    private byte[] image;
    private UserView author;

    public RecipeView(UUID recipeId, String title, String description, UUID authorId) {
        this.recipeId = recipeId;
        this.title = title;
        this.description = description;
        this.author = new UserView(authorId);
    }
}
