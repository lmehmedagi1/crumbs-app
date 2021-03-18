package com.crumbs.recipeservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "diets")
public class Diet {

    @Id
    @Type(type="uuid-char")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @NotNull
    @NotEmpty
    private String title;

    @NotNull
    @NotEmpty
    private String description;

    @NotNull
    @NotEmpty
    private Integer duration;

    @NotNull
    @NotEmpty
    private boolean is_private;

    @ManyToMany
    @JoinTable(
            name = "diet_recipes",
            joinColumns = @JoinColumn(
                    name = "diet_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "recipe_id", referencedColumnName = "id"))
    private List<Recipe> recipes;
}
