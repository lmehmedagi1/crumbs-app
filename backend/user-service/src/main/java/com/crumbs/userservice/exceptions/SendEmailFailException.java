package com.crumbs.userservice.exceptions;

public class SendEmailFailException extends RuntimeException {
    public SendEmailFailException() {
        super("Email could not be sent");
    }

    public SendEmailFailException(String message) {
        super(message);
    }
}
