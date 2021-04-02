package com.crumbs.recipeservice.utility;

import com.crumbs.recipeservice.controllers.RecipeController;
import com.crumbs.recipeservice.models.Recipe;
import com.crumbs.recipeservice.projections.RecipeView;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RecipeViewModelAssembler implements RepresentationModelAssembler<RecipeView, EntityModel<RecipeView>> {
    @Override
    public EntityModel<RecipeView> toModel(RecipeView recipeView) {
        return EntityModel.of(recipeView, linkTo(methodOn(RecipeController.class).getRecipe(recipeView.getId())).withSelfRel(),
                linkTo(methodOn(RecipeController.class).getAllRecipes()).withRel("recipes"));
    }
}