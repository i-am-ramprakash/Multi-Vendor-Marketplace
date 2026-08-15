package com.marketplace.cart.domain.exception;

public class InvalidCartStateException extends RuntimeException {

    public InvalidCartStateException(String currentStatus, String attemptedAction) {
        super(String.format("Cannot %s cart in %s state", attemptedAction, currentStatus));
    }
}