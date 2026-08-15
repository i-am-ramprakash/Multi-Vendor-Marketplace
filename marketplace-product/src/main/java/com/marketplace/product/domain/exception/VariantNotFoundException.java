package com.marketplace.product.domain.exception;

public class VariantNotFoundException extends RuntimeException {

    public VariantNotFoundException(String identifier) {
        super("Variant not found: " + identifier);
    }

    public VariantNotFoundException(Long id) {
        super("Variant not found with id: " + id);
    }
}