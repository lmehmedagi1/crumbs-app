package com.crumbs.recipeservice.util;

import com.crumbs.recipeservice.controllers.IngredientController;
import com.crumbs.recipeservice.models.Ingredient;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class IngredientModelAssembler implements RepresentationModelAssembler<Ingredient, EntityModel<Ingredient>> {
    @Override
    public EntityModel<Ingredient> toModel(Ingredient ingredient) {
        return EntityModel.of(ingredient, linkTo(methodOn(IngredientController.class).getIngredient(ingredient.getId().toString())).withSelfRel(),
                linkTo(methodOn(IngredientController.class).getAllIngredients()).withRel("ingredients"));
    }

    @Override
    public CollectionModel<EntityModel<Ingredient>> toCollectionModel(Iterable<? extends Ingredient> entities) {
        return null;
    }
}

