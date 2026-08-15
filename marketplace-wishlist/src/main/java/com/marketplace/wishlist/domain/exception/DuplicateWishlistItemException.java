package com.marketplace.wishlist.domain.exception;

public class DuplicateWishlistItemException extends RuntimeException {

    public DuplicateWishlistItemException(Long productId) {
        super("Product already exists in wishlist: " + productId);
    }

    public DuplicateWishlistItemException(Long productId, Long variantId) {
        super(String.format("Product %d with variant %d already exists in wishlist", productId, variantId));
    }
}