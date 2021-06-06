package com.crumbs.reviewservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {

    private UUID id;

    @NotBlank
    @Size(min = 5, message = "Ingredient name must be at least 5 characters long!")
    @Size(max = 30, message = "Ingredient name exceeds allowed limit of 30 characters!")
    @Pattern(regexp = "^[A-Za-zščćžđŠČĆŽĐ\\s]+$", flags = Pattern.Flag.UNICODE_CASE, message = "Ingredient name can only contain letters and whitespaces!")
    private String name;
}
