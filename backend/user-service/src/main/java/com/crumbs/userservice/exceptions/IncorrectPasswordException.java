package com.crumbs.userservice.exceptions;

public class IncorrectPasswordException extends RuntimeException {
    public IncorrectPasswordException() {
        super("Password is incorrect");
    }

    public IncorrectPasswordException(String message) {
        super(message);
    }
}
