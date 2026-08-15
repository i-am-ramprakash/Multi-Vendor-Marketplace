package com.marketplace.order.domain.exception;

public class EmptyCartException extends RuntimeException {

    public EmptyCartException() {
        super("Cannot checkout with an empty cart");
    }

    public EmptyCartException(String message) {
        super(message);
    }
}