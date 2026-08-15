package com.marketplace.cart.domain.valueobject;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class CartId {

    private final Long value;

    private CartId(Long value) {
        this.value = value;
    }

    public static CartId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("Cart ID cannot be null");
        }
        return new CartId(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}