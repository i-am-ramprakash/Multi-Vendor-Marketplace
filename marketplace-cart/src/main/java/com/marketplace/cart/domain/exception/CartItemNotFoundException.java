package com.marketplace.cart.domain.exception;

public class CartItemNotFoundException extends RuntimeException {

    public CartItemNotFoundException(Long itemId) {
        super("Cart item not found with ID: " + itemId);
    }

    public CartItemNotFoundException(Long productId, Long variantId) {
        super("Cart item not found for product: " + productId + " and variant: " + variantId);
    }
}