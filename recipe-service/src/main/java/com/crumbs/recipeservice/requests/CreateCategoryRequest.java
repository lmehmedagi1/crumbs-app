package com.crumbs.recipeservice.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class CreateCategoryRequest {
    @NonNull
    @NotBlank
    private String name;

    @NonNull
    @NotBlank
    private String name1;
}
