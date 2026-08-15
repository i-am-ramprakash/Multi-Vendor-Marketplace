package com.marketplace.commission.domain.valueobject;

import lombok.Getter;

@Getter
public enum SettlementStatus {
    PENDING("Pending", "Settlement is pending"),
    PROCESSING("Processing", "Settlement is being processed"),
    COMPLETED("Completed", "Settlement has been completed"),
    FAILED("Failed", "Settlement failed"),
    CANCELLED("Cancelled", "Settlement was cancelled");

    private final String displayName;
    private final String description;

    SettlementStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public boolean canTransitionTo(SettlementStatus newStatus) {
        return switch (this) {
            case PENDING -> newStatus == PROCESSING || newStatus == CANCELLED;
            case PROCESSING -> newStatus == COMPLETED || newStatus == FAILED;
            case COMPLETED -> false;
            case FAILED -> newStatus == PENDING; // Can retry
            case CANCELLED -> false;
        };
    }
}