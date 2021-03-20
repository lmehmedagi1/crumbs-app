package com.crumbs.reviewservice.model;

import com.crumbs.reviewservice.utility.annotation.NullOrNotBlank;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reviews")
public class Review implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @NotBlank
    @Column(name = "user_id")
    @JsonProperty("user_id")
    private UUID userId;

    @NotBlank
    @Column(name = "recipe_id")
    @JsonProperty("recipe_id")
    private UUID recipeId;

    @NotNull
    @Column(name = "is_liked")
    @JsonProperty("is_liked")
    private Boolean isLiked;

    @Min(value = 1, message = "Rating must be between 1-5 inclusive!")
    @Max(value = 5, message = "Rating must be between 1-5 inclusive!")
    private Integer rating;

    @NullOrNotBlank
    @Size(max = 1000, message = "Comment exceeds allowed limit of 1000 characters!")
    @Pattern(regexp = "^[A-Za-z0-9 _.,!?\"'-/]*$", flags = Pattern.Flag.UNICODE_CASE, message = "Comment can only contain letters, numbers, spaces, and punctuation.")
    private String comment;

}
