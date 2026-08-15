package com.marketplace.notification.domain.exception;

public class DeadLetterNotFoundException extends RuntimeException {

    public DeadLetterNotFoundException(Long id) {
        super("Dead letter message not found with id: " + id);
    }
}