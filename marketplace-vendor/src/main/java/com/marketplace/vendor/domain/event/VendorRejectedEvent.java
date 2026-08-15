package com.marketplace.vendor.domain.event;

import lombok.Getter;

@Getter
public class VendorRejectedEvent extends DomainEvent {

    private final Long vendorId;
    private final Long userId;
    private final String storeName;
    private final Long rejectedBy;
    private final String rejectionReason;

    public VendorRejectedEvent(Long vendorId, Long userId, String storeName, Long rejectedBy, String rejectionReason) {
        super();
        this.vendorId = vendorId;
        this.userId = userId;
        this.storeName = storeName;
        this.rejectedBy = rejectedBy;
        this.rejectionReason = rejectionReason;
    }
}