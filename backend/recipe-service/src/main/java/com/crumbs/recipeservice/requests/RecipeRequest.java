package com.crumbs.recipeservice.requests;

import com.crumbs.recipeservice.utility.annotation.NullOrNotBlank;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class RecipeRequest {

    @NotBlank
    @Size(min = 5, message = "Recipe title must be at least 5 characters long!")
    @Size(max = 50, message = "Recipe title exceeds allowed limit of 50 characters!")
    @Pattern(regexp = "^[a-zA-Z0-9ščćžđ_\\s\\\\.,\\-!?%']*$", flags = Pattern.Flag.UNICODE_CASE, message = "Recipe description can only contain letters, numbers " +
            "and special characters: _ \\ . , - ! ? % '")
    private String title;

    @NullOrNotBlank
    @Size(max = 500, message = "Recipe description exceeds allowed limit of 500 characters!")
    @Pattern(regexp = "^[a-zA-Z0-9šđčćžŠĐČĆŽ_\\s\\\\.,\\-!?%']*$", flags = Pattern.Flag.UNICODE_CASE, message = "Recipe description can only contain letters, numbers " +
            "and special characters: _ \\ . , - ! ? % '")
    private String description;

    @NotBlank
    @Size(min = 50, message = "Recipe method must be at least 50 characters long!")
    @Size(max = 3000, message = "Recipe method exceeds allowed limit of 3000 characters!")
    @Pattern(regexp = "^[a-zA-Z0-9šđčćžŠĐČĆŽ_\\s\\\\.,\\-!?%']*$", flags = Pattern.Flag.UNICODE_CASE, message = "Recipe method can only contain letters, numbers " +
            "and special characters: _ \\ . , - ! ? % '")
    private String method;

    @Size(max = 3000, message = "Advice method exceeds allowed limit of 3000 characters!")
    @Pattern(regexp = "^[a-zA-Z0-9ščćđžČŠĆĐŽ_\\s\\\\.,\\-!?%]*$", flags = Pattern.Flag.UNICODE_CASE, message = "Advice method can only contain letters, numbers " +
            "and special characters: _ \\ . , - ! ? %")
    private String advice;

    @NotNull
    @Min(value = 1, message = "Preparation time in minutes must be greater than zero!")
    @JsonProperty(value = "preparation_time")
    private Integer preparationTime;

    private List<UUID> categories;

    private List<UUID> ingredients;

    private List<String> images;
}
