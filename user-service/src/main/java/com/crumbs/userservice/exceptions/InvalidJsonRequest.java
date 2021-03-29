package com.crumbs.userservice.exceptions;

public class InvalidJsonRequest extends RuntimeException {

    public InvalidJsonRequest() {
        super("Malformed/Invalid JSON request");
    }

    public InvalidJsonRequest(String message) {
        super(message);
    }
}
