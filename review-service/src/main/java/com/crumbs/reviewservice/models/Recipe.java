package com.crumbs.reviewservice.models;

import com.crumbs.reviewservice.utility.annotation.NullOrNotBlank;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {

    private UUID id;

    private UUID userId;

    @NotBlank
    @Size(min = 5, message = "Recipe title must be at least 5 characters long!")
    @Size(max = 50, message = "Recipe title exceeds allowed limit of 50 characters!")
    @Pattern(regexp = "^[a-zA-Z0-9_\\s\\\\.,\\-!?%]*$", flags = Pattern.Flag.UNICODE_CASE, message = "Recipe title can only contain letters, numbers " +
            "and special characters: _ \\ . , - ! ? %")
    private String title;

    @NullOrNotBlank
    @Size(max = 500, message = "Recipe description exceeds allowed limit of 500 characters!")
    @Pattern(regexp = "^[a-zA-Z0-9_\\s\\\\.,\\-!?%]*$", flags = Pattern.Flag.UNICODE_CASE, message = "Recipe description can only contain letters, numbers " +
            "and special characters: _ \\ . , - ! ? %")
    private String description;

    @NotBlank
    @Size(min = 50, message = "Recipe method must be at least 50 characters long!")
    @Size(max = 3000, message = "Recipe method exceeds allowed limit of 3000 characters!")
    @Pattern(regexp = "^[a-zA-Z0-9_\\s\\\\.,\\-!?%]*$", flags = Pattern.Flag.UNICODE_CASE, message = "Recipe method can only contain letters, numbers " +
            "and special characters: _ \\ . , - ! ? %")
    private String method;

    private Set<Category> categories;

    private Set<Ingredient> ingredients;

    @JsonManagedReference
    private List<Image> images;
}
