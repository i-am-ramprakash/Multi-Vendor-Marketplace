package com.marketplace.cart.domain.event;

import lombok.Getter;

@Getter
public class ItemAddedToCartEvent extends DomainEvent {

    private final Long cartId;
    private final Long userId;
    private final Long productId;
    private final Long variantId;
    private final int quantity;

    public ItemAddedToCartEvent(Object source, Long cartId, Long userId, Long productId,
                                Long variantId, int quantity) {
        super(source);
        this.cartId = cartId;
        this.userId = userId;
        this.productId = productId;
        this.variantId = variantId;
        this.quantity = quantity;
    }
}