package com.crumbs.recipeservice.utility.apierror;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class ApiSubError {
    private String field;
    private String message;
}
