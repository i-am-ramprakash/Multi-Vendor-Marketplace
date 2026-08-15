package com.marketplace.product.domain.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String identifier) {
        super("Product not found: " + identifier);
    }

    public ProductNotFoundException(Long id) {
        super("Product not found with id: " + id);
    }
}