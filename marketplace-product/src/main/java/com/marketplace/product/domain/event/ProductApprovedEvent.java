package com.marketplace.product.domain.event;

import lombok.Getter;

@Getter
public class ProductApprovedEvent extends DomainEvent {

    private final Long productId;
    private final Long vendorId;
    private final String productName;
    private final Long approvedBy;

    public ProductApprovedEvent(Long productId, Long vendorId, String productName, Long approvedBy) {
        super();
        this.productId = productId;
        this.vendorId = vendorId;
        this.productName = productName;
        this.approvedBy = approvedBy;
    }
}