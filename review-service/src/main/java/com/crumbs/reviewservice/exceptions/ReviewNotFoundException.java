package com.crumbs.reviewservice.exceptions;

public class ReviewNotFoundException extends RuntimeException {

    public ReviewNotFoundException() {
        super("Review not found!");
    }

    public ReviewNotFoundException(String message) {
        super(message);
    }
}
