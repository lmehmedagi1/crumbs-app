package com.crumbs.recipeservice.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class UpdateDietRequest {
    @NonNull
    @NotBlank
    private String id;

    @NonNull
    @NotBlank
    private String title;

    @NonNull
    @NotBlank
    private String description;

    @NonNull
    private Integer duration;

    @NotNull
    private Boolean isPrivate;
}
