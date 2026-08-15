package com.marketplace.product.domain.exception;

public class InsufficientInventoryException extends RuntimeException {

    public InsufficientInventoryException(String variantName, int requested, int available) {
        super("Insufficient inventory for variant '" + variantName + "': requested " + requested + ", available " + available);
    }

    public InsufficientInventoryException(Long variantId, int requested, int available) {
        super("Insufficient inventory for variant " + variantId + ": requested " + requested + ", available " + available);
    }
}