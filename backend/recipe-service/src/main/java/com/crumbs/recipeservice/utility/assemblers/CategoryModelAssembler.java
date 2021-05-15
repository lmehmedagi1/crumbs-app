package com.crumbs.recipeservice.utility.assemblers;

import com.crumbs.recipeservice.controllers.CategoryController;
import com.crumbs.recipeservice.models.Category;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CategoryModelAssembler implements RepresentationModelAssembler<Category, EntityModel<Category>> {
    @Override
    public EntityModel<Category> toModel(Category category) {
        return EntityModel.of(category, linkTo(methodOn(CategoryController.class).getCategory(category.getId())).withSelfRel());
    }
}