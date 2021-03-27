package com.crumbs.notificationservice.exceptions;

public class NotificationNotFoundException extends RuntimeException {

    public NotificationNotFoundException() {
        super("Notification not found");
    }

    public NotificationNotFoundException(String message) {
        super(message);
    }
}