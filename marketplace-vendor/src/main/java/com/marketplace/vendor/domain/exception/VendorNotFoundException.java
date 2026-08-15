package com.marketplace.vendor.domain.exception;

public class VendorNotFoundException extends RuntimeException {

    public VendorNotFoundException(String identifier) {
        super("Vendor not found: " + identifier);
    }

    public VendorNotFoundException(Long id) {
        super("Vendor not found with id: " + id);
    }
}