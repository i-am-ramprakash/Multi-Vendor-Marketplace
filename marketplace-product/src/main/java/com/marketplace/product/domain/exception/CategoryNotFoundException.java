package com.marketplace.product.domain.exception;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(String identifier) {
        super("Category not found: " + identifier);
    }

    public CategoryNotFoundException(Long id) {
        super("Category not found with id: " + id);
    }
}