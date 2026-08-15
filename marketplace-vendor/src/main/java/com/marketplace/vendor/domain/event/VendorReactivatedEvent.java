package com.marketplace.vendor.domain.event;

import lombok.Getter;

@Getter
public class VendorReactivatedEvent extends DomainEvent {

    private final Long vendorId;
    private final Long userId;
    private final String storeName;
    private final Long reactivatedBy;

    public VendorReactivatedEvent(Long vendorId, Long userId, String storeName, Long reactivatedBy) {
        super();
        this.vendorId = vendorId;
        this.userId = userId;
        this.storeName = storeName;
        this.reactivatedBy = reactivatedBy;
    }
}