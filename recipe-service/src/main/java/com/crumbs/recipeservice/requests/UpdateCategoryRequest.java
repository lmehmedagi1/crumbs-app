package com.crumbs.recipeservice.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class UpdateCategoryRequest {

    @NonNull
    @NotBlank
    private String id;

    @NotNull
    @NotBlank
    private String name;
}
