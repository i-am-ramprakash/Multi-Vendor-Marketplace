package com.marketplace.order.domain.valueobject;

import lombok.Getter;

@Getter
public enum OrderItemStatus {
    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    PROCESSING("Processing"),
    SHIPPED("Shipped"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled"),
    REFUNDED("Refunded");

    private final String displayName;

    OrderItemStatus(String displayName) {
        this.displayName = displayName;
    }

    public boolean canTransitionTo(OrderItemStatus newStatus) {
        return switch (this) {
            case PENDING -> newStatus == CONFIRMED || newStatus == CANCELLED;
            case CONFIRMED -> newStatus == PROCESSING || newStatus == CANCELLED;
            case PROCESSING -> newStatus == SHIPPED || newStatus == CANCELLED;
            case SHIPPED -> newStatus == DELIVERED || newStatus == REFUNDED;
            case DELIVERED -> newStatus == REFUNDED;
            case CANCELLED -> false;
            case REFUNDED -> false;
        };
    }

    public boolean isActive() {
        return this != CANCELLED && this != REFUNDED;
    }
}