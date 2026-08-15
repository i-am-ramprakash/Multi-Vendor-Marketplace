package com.marketplace.product.domain.exception;

public class InvalidProductStateException extends RuntimeException {

    public InvalidProductStateException(String message) {
        super(message);
    }

    public InvalidProductStateException(String currentStatus, String requestedAction) {
        super("Cannot " + requestedAction + " product in " + currentStatus + " status");
    }
}