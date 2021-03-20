package com.crumbs.reviewservice.model;

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

    @NotNull
    @Column(name = "user_id")
    private UUID userId;

    @NotNull
    @Column(name = "recipe_id")
    private UUID recipeId;

    @NotNull
    @Column(name = "is_liked")
    private Boolean isLiked;

    @Min(1)
    @Max(5)
    private Integer rating;

    @NotEmpty
    @Size(min = 1, max = 8000, message = "Comment exceeds allowed limit of 8000 characters!")
    @Pattern(regexp = "^[A-Za-z0-9 _.,!\"'-/]*$", flags = Pattern.Flag.UNICODE_CASE, message = "Comment can only contain letters, numbers, spaces, and punctuation.")
    private String comment;
}
