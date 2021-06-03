package com.crumbs.recipeservice.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "diets")
public class Diet {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @NotNull
    @Column(name = "user_id")
    private UUID userId;

    @NotBlank
    @Size(min = 5, message = "Diet title must be at least 5 characters long!")
    @Size(max = 50, message = "Diet title exceeds allowed limit of 50 characters!")
    @Pattern(regexp = "^[a-zA-Z0-9_\\s\\\\.,\\-!?%]*$", flags = Pattern.Flag.UNICODE_CASE, message = "Diet title can only contain letters, numbers " +
            "and special characters: _ \\ . , - ! ? %")
    private String title;

    @NotBlank
    @Size(max = 2000, message = "Diet description exceeds allowed limit of 2000 characters!")
    @Pattern(regexp = "^[a-zA-Z0-9_\\s\\\\.,\\-!?%]*$", flags = Pattern.Flag.UNICODE_CASE, message = "Diet description can only contain letters, numbers " +
            "and special characters: _ \\ . , - ! ? %")
    private String description;

    @NotNull
    private Integer duration;

    @NotNull
    @JsonProperty("is_private")
    @Column(name = "is_private")
    private Boolean isPrivate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_modify")
    private LocalDateTime lastModify;

    @ManyToMany
    @JoinTable(
            name = "diet_recipes",
            joinColumns = @JoinColumn(
                    name = "diet_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "recipe_id", referencedColumnName = "id"))
    private List<Recipe> recipes;
}
