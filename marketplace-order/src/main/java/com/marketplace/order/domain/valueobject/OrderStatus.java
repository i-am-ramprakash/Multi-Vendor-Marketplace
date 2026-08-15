package com.marketplace.order.domain.valueobject;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING("Pending", "Order has been placed"),
    CONFIRMED("Confirmed", "Order has been confirmed by vendor"),
    PROCESSING("Processing", "Order is being processed"),
    SHIPPED("Shipped", "Order has been shipped"),
    DELIVERED("Delivered", "Order has been delivered"),
    CANCELLED("Cancelled", "Order has been cancelled"),
    REFUNDED("Refunded", "Order has been refunded");

    private final String displayName;
    private final String description;

    OrderStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public boolean canTransitionTo(OrderStatus newStatus) {
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

    public boolean canBeCancelled() {
        return this == PENDING || this == CONFIRMED || this == PROCESSING;
    }

    public boolean canBeRefunded() {
        return this == SHIPPED || this == DELIVERED;
    }
}