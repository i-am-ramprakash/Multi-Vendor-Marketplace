package com.marketplace.order.domain.exception;

public class InvalidOrderStateException extends RuntimeException {

    public InvalidOrderStateException(String currentStatus, String attemptedAction) {
        super(String.format("Cannot %s order in %s state", attemptedAction, currentStatus));
    }
}