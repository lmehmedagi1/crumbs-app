package com.crumbs.recipeservice.projections;

import com.crumbs.recipeservice.models.Image;

import java.util.List;
import java.util.UUID;

public interface RecipeView {
    UUID getId();
    String getTitle();
    String getDescription();
    List<Image> getImages();
}
