package com.marketplace.cart.domain.event;

import lombok.Getter;

@Getter
public class CartClearedEvent extends DomainEvent {

    private final Long cartId;
    private final Long userId;

    public CartClearedEvent(Object source, Long cartId, Long userId) {
        super(source);
        this.cartId = cartId;
        this.userId = userId;
    }
}