package com.crumbs.recipeservice.projections;

import lombok.Data;

import java.util.UUID;

@Data

public class CategoryView {
    private UUID id;
    private String name;
    private CategoryTypeView categoryTypeView;

    public CategoryView(UUID id, String name, UUID type_id, String typeName) {
        this.id = id;
        this.name = name;
        this.categoryTypeView = new CategoryTypeView(type_id, typeName);
    }
}
