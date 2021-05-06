package com.crumbs.recipeservice.utility.assemblers;

import com.crumbs.recipeservice.controllers.RecipeController;
import com.crumbs.recipeservice.models.Recipe;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RecipeModelAssembler implements RepresentationModelAssembler<Recipe, EntityModel<Recipe>> {
    @Override
    public EntityModel<Recipe> toModel(Recipe recipe) {
        return EntityModel.of(recipe, linkTo(methodOn(RecipeController.class).getRecipe(recipe.getId())).withSelfRel());
    }
}