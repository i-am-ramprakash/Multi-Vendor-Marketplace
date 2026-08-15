package com.marketplace.vendor.domain.exception;

public class InvalidVendorStateException extends RuntimeException {

    public InvalidVendorStateException(String message) {
        super(message);
    }

    public InvalidVendorStateException(String currentStatus, String requestedAction) {
        super("Cannot " + requestedAction + " vendor in " + currentStatus + " status");
    }
}