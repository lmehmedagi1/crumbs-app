package com.crumbs.recipeservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class IngredientNotFoundException extends RuntimeException {
    public IngredientNotFoundException(String id) {
        super("The specified ingredient with id " + id + " does not exist.");
    }
}
