package com.crumbs.recipeservice.models;

import com.crumbs.recipeservice.utility.annotation.NullOrNotBlank;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recipes")
public class Recipe {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

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

    @ManyToMany
    @JoinTable(
            name = "recipe_categories",
            joinColumns = @JoinColumn(
                    name = "recipe_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "category_id", referencedColumnName = "id"))
    private Set<Category> categories;

    @ManyToMany
    @JoinTable(
            name = "recipe_ingredients",
            joinColumns = @JoinColumn(
                    name = "recipe_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "ingredient_id", referencedColumnName = "id"))
    private Set<Ingredient> ingredients;

    @JsonManagedReference
    @OneToMany(mappedBy = "recipe")
    private List<Image> images;
}
