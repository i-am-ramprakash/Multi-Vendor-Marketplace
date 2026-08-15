package com.marketplace.cart.domain.exception;

public class CartNotFoundException extends RuntimeException {

    public CartNotFoundException(Long cartId) {
        super("Cart not found with ID: " + cartId);
    }

    public CartNotFoundException(String message) {
        super(message);
    }
}