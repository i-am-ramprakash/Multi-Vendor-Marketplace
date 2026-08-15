package com.marketplace.shared.exception;

public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }

    public static ValidationException of(String message) {
        return new ValidationException(message);
    }
}
