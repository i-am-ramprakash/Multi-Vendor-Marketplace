package com.marketplace.vendor.domain.event;

import lombok.Getter;

@Getter
public class VendorRegisteredEvent extends DomainEvent {

    private final Long vendorId;
    private final Long userId;
    private final String storeName;
    private final String storeSlug;
    private final String contactEmail;

    public VendorRegisteredEvent(Long vendorId, Long userId, String storeName, String storeSlug, String contactEmail) {
        super();
        this.vendorId = vendorId;
        this.userId = userId;
        this.storeName = storeName;
        this.storeSlug = storeSlug;
        this.contactEmail = contactEmail;
    }
}