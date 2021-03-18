package com.crumbs.recipeservice.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class UpdateRecipeRequest {
    @NonNull
    @NotBlank
    private String id;

    @NonNull
    @NotBlank
    private String title;

    @NonNull
    @NotBlank
    private String method;

    private String description;
}
