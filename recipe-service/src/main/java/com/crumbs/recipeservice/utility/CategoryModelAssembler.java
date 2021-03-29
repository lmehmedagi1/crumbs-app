package com.crumbs.recipeservice.utility;

import com.crumbs.recipeservice.models.Category;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class CategoryModelAssembler implements RepresentationModelAssembler<Category, EntityModel<Category>> {
    @Override
    public EntityModel<Category> toModel(Category entity) {
        return null;
    }

    @Override
    public CollectionModel<EntityModel<Category>> toCollectionModel(Iterable<? extends Category> entities) {
        return null;
    }
}
