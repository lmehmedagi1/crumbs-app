package com.crumbs.userservice.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    private String altMessage;

    public UserAlreadyExistsException() {
        super("User already exists");
    }

    public UserAlreadyExistsException(String altMessage) {
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
