package com.crumbs.userservice.exceptions;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException() {
        super("Token expired");
    }

    public TokenExpiredException(String message) {
        super(message);
    }
}
