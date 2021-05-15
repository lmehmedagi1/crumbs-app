package com.crumbs.userservice.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    private String errorMessage;

    public UserAlreadyExistsException() {
        super("User already exists");
    }

    public UserAlreadyExistsException(String errorMessage) {
        this();
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
