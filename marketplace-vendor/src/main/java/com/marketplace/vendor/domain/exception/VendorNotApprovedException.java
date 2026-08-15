package com.marketplace.vendor.domain.exception;

public class VendorNotApprovedException extends RuntimeException {

    public VendorNotApprovedException(Long vendorId) {
        super("Vendor is not approved: " + vendorId);
    }

    public VendorNotApprovedException(String storeSlug) {
        super("Vendor store is not approved: " + storeSlug);
    }
}