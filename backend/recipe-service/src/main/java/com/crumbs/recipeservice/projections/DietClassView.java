package com.crumbs.recipeservice.projections;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DietClassView {
    private UUID id;
    private String title;
    private String description;
    private Integer duration;
    private UserClassView author;
    private List<RecipeView> recipes;

    public DietClassView(UUID id, String title, String description, Integer duration, UUID authorId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.author = new UserClassView(authorId);
    }
}
