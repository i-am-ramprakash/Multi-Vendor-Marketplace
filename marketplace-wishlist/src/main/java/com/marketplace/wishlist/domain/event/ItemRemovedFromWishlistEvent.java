package com.marketplace.wishlist.domain.event;

import lombok.Getter;

@Getter
public class ItemRemovedFromWishlistEvent extends DomainEvent {

    private final Long wishlistId;
    private final Long userId;
    private final Long productId;
    private final Long variantId;

    public ItemRemovedFromWishlistEvent(Object source, Long wishlistId, Long userId,
                                        Long productId, Long variantId) {
        super(source);
        this.wishlistId = wishlistId;
        this.userId = userId;
        this.productId = productId;
        this.variantId = variantId;
    }
}