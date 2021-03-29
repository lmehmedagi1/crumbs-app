package com.crumbs.recipeservice.utility;

import com.crumbs.recipeservice.controllers.DietController;
import com.crumbs.recipeservice.models.Diet;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DietModelAssembler implements RepresentationModelAssembler<Diet, EntityModel<Diet>> {
    @Override
    public EntityModel<Diet> toModel(Diet diet) {
        return EntityModel.of(diet, linkTo(methodOn(DietController.class).getDiet(diet.getId())).withSelfRel(),
                linkTo(methodOn(DietController.class).getAllDiets()).withRel("diets"));
    }
}