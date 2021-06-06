package com.crumbs.recipeservice.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {
    @NotBlank
    @Size(min = 5, message = "Category name must be at least 5 characters long!")
    @Size(max = 30, message = "Category name exceeds allowed limit of 30 characters!")
    @Pattern(regexp = "^[A-Za-zščćžđŠČĆŽĐ\\s]+$", flags = Pattern.Flag.UNICODE_CASE, message = "Category name can only contain letters and whitespaces!")
    private String name;
}
