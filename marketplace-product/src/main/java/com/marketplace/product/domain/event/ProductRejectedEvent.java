package com.marketplace.product.domain.event;

import lombok.Getter;

@Getter
public class ProductRejectedEvent extends DomainEvent {

    private final Long productId;
    private final Long vendorId;
    private final String productName;
    private final Long rejectedBy;
    private final String rejectionReason;

    public ProductRejectedEvent(Long productId, Long vendorId, String productName, Long rejectedBy, String rejectionReason) {
        super();
        this.productId = productId;
        this.vendorId = vendorId;
        this.productName = productName;
        this.rejectedBy = rejectedBy;
        this.rejectionReason = rejectionReason;
    }
}