package com.marketplace.product.domain.event;

import lombok.Getter;

@Getter
public class ProductCreatedEvent extends DomainEvent {

    private final Long productId;
    private final Long vendorId;
    private final String productName;
    private final String productSlug;

    public ProductCreatedEvent(Long productId, Long vendorId, String productName, String productSlug) {
        super();
        this.productId = productId;
        this.vendorId = vendorId;
        this.productName = productName;
        this.productSlug = productSlug;
    }
}