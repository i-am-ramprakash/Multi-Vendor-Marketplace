package com.marketplace.notification.domain.exception;

public class NotificationNotFoundException extends RuntimeException {

    public NotificationNotFoundException(Long id) {
        super("Notification not found with id: " + id);
    }

    public NotificationNotFoundException(String referenceId) {
        super("Notification not found with reference id: " + referenceId);
    }
}