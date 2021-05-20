package com.crumbs.userservice.exceptions;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() {
        super("Unauthorized request");
    }

    public UnauthorizedException(String message) {
        super(message);
    }
}
