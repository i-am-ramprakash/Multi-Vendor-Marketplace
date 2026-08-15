package com.marketplace.cart.domain.event;

import lombok.Getter;

@Getter
public class ItemRemovedFromCartEvent extends DomainEvent {

    private final Long cartId;
    private final Long userId;
    private final Long productId;
    private final Long variantId;

    public ItemRemovedFromCartEvent(Object source, Long cartId, Long userId,
                                    Long productId, Long variantId) {
        super(source);
        this.cartId = cartId;
        this.userId = userId;
        this.productId = productId;
        this.variantId = variantId;
    }
}