package com.crumbs.recipeservice.responses;

import com.crumbs.recipeservice.models.Recipe;
import com.crumbs.recipeservice.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.EntityModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeWithDetails {
    private EntityModel<Recipe> recipe;
    private EntityModel<User> author;
    private Double rating;
}
