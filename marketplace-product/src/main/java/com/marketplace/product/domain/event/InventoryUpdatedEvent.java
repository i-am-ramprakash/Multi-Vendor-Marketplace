package com.marketplace.product.domain.event;

import lombok.Getter;

@Getter
public class InventoryUpdatedEvent extends DomainEvent {

    private final Long variantId;
    private final Long productId;
    private final String variantName;
    private final int previousQuantity;
    private final int newQuantity;
    private final String movementType;

    public InventoryUpdatedEvent(Long variantId, Long productId, String variantName, 
                                int previousQuantity, int newQuantity, String movementType) {
        super();
        this.variantId = variantId;
        this.productId = productId;
        this.variantName = variantName;
        this.previousQuantity = previousQuantity;
        this.newQuantity = newQuantity;
        this.movementType = movementType;
    }
}