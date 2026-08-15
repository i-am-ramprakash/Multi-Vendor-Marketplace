package com.marketplace.wishlist.domain.exception;

public class WishlistItemNotFoundException extends RuntimeException {

    public WishlistItemNotFoundException(Long itemId) {
        super("Wishlist item not found with ID: " + itemId);
    }

    public WishlistItemNotFoundException(Long productId, Long variantId) {
        super("Wishlist item not found for product: " + productId + " and variant: " + variantId);
    }
}