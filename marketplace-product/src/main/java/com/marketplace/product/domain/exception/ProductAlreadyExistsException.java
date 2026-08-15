package com.marketplace.product.domain.exception;

public class ProductAlreadyExistsException extends RuntimeException {

    public ProductAlreadyExistsException(String message) {
        super(message);
    }

    public ProductAlreadyExistsException(String field, String value) {
        super("Product with " + field + " already exists: " + value);
    }
}