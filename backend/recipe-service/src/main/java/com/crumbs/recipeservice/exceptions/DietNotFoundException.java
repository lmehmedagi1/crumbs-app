package com.crumbs.recipeservice.exceptions;

public class DietNotFoundException extends RuntimeException {
    public DietNotFoundException() {
        super("Diet not found");
    }

    public DietNotFoundException(String message) {
        super(message);
    }
}
