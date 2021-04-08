package com.crumbs.recipeservice.exceptions;

public class UserNotFoundException extends RuntimeException {

    private String altMessage;

    public UserNotFoundException() {
        super("User not found");
    }

    public UserNotFoundException(String altMessage) {
        this();
        this.altMessage = altMessage;
    }

    public String getAltMessage() {
        return altMessage;
    }

    public void setAltMessage(String altMessage) {
        this.altMessage = altMessage;
    }

}
