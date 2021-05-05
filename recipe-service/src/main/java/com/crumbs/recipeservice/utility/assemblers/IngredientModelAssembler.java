package com.crumbs.recipeservice.utility.assemblers;

import com.crumbs.recipeservice.controllers.IngredientController;
import com.crumbs.recipeservice.models.Ingredient;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class IngredientModelAssembler implements RepresentationModelAssembler<Ingredient, EntityModel<Ingredient>> {
    @Override
    public EntityModel<Ingredient> toModel(Ingredient ingredient) {
        return EntityModel.of(ingredient, linkTo(methodOn(IngredientController.class).getIngredient(ingredient.getId())).withSelfRel());
    }
}

