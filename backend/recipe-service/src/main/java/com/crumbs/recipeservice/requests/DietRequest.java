package com.crumbs.recipeservice.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DietRequest {

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
    private Boolean isPrivate;

    private List<UUID> recipes;
}
