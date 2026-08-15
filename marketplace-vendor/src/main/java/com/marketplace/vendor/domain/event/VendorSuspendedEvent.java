package com.marketplace.vendor.domain.event;

import lombok.Getter;

@Getter
public class VendorSuspendedEvent extends DomainEvent {

    private final Long vendorId;
    private final Long userId;
    private final String storeName;
    private final Long suspendedBy;
    private final String suspensionReason;

    public VendorSuspendedEvent(Long vendorId, Long userId, String storeName, Long suspendedBy, String suspensionReason) {
        super();
        this.vendorId = vendorId;
        this.userId = userId;
        this.storeName = storeName;
        this.suspendedBy = suspendedBy;
        this.suspensionReason = suspensionReason;
    }
}