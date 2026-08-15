package com.marketplace.cart.domain.valueobject;

import lombok.Getter;

@Getter
public enum CartStatus {
    ACTIVE("Active"),
    MERGED("Merged"),
    ABANDONED("Abandoned"),
    CONVERTED("Converted to Order");

    private final String displayName;

    CartStatus(String displayName) {
        this.displayName = displayName;
    }

    public boolean canBeModified() {
        return this == ACTIVE;
    }

    public boolean canBeCheckedOut() {
        return this == ACTIVE;
    }
}