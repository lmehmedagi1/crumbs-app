package com.crumbs.recipeservice.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class IngredientRequest {
    @NonNull
    @NotBlank
    @Size(min = 1, max = 50, message = "Ingredient exceeds allowed limit of 50 characters!")

    @Pattern(regexp = "^[A-Za-z ]*$", flags = Pattern.Flag.UNICODE_CASE, message = "Ingredient can only contain letters and spaces.")
    private String name;
}
