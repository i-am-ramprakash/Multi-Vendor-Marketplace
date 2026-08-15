package com.marketplace.vendor.domain.exception;

public class VendorAlreadyExistsException extends RuntimeException {

    public VendorAlreadyExistsException(String message) {
        super(message);
    }

    public VendorAlreadyExistsException(Long userId) {
        super("Vendor already exists for user: " + userId);
    }

    public VendorAlreadyExistsException(String field, String value) {
        super("Vendor with " + field + " already exists: " + value);
    }
}