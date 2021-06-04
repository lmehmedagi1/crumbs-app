package com.crumbs.recipeservice.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class FilterRecipesRequest {

    private String title;
    private List<UUID> categories;

}
