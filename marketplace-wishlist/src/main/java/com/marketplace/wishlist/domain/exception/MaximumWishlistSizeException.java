package com.marketplace.wishlist.domain.exception;

public class MaximumWishlistSizeException extends RuntimeException {

    public MaximumWishlistSizeException(int maxSize) {
        super("Wishlist cannot have more than " + maxSize + " items");
    }
}