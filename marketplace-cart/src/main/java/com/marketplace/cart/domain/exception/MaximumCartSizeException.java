package com.marketplace.cart.domain.exception;

public class MaximumCartSizeException extends RuntimeException {

    public MaximumCartSizeException(int maxSize) {
        super("Cart cannot have more than " + maxSize + " items");
    }
}