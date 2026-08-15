package com.marketplace.vendor.domain.event;

import lombok.Getter;

@Getter
public class VendorApprovedEvent extends DomainEvent {

    private final Long vendorId;
    private final Long userId;
    private final String storeName;
    private final Long approvedBy;

    public VendorApprovedEvent(Long vendorId, Long userId, String storeName, Long approvedBy) {
        super();
        this.vendorId = vendorId;
        this.userId = userId;
        this.storeName = storeName;
        this.approvedBy = approvedBy;
    }
}