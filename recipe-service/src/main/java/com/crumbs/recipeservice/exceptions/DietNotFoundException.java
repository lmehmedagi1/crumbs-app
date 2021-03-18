package com.crumbs.recipeservice.exceptions;

public class DietNotFoundException extends RuntimeException {
    public DietNotFoundException(String message) {
        super(message);
    }
}
