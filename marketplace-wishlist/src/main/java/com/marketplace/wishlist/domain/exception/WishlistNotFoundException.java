package com.marketplace.wishlist.domain.exception;

public class WishlistNotFoundException extends RuntimeException {

    public WishlistNotFoundException(Long wishlistId) {
        super("Wishlist not found with ID: " + wishlistId);
    }

    public WishlistNotFoundException(String message) {
        super(message);
    }
}