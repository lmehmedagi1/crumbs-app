package com.crumbs.recipeservice.requests;

import com.crumbs.recipeservice.models.Category;
import com.crumbs.recipeservice.models.Image;
import com.crumbs.recipeservice.models.Ingredient;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
public class RecipeRequest {

    @NonNull
    @NotBlank
    private String title;

    @NonNull
    @NotBlank
    private String method;

    private String description;
}
