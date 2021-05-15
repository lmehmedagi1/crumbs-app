package com.crumbs.notificationservice.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("User not found");
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public String getAltMessage() {
        return "User with specified parameters does not exist!";
    }
}
