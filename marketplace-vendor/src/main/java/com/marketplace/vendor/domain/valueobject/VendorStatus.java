package com.marketplace.vendor.domain.valueobject;

public enum VendorStatus {
    PENDING("Pending approval"),
    APPROVED("Approved - Active"),
    REJECTED("Application rejected"),
    SUSPENDED("Suspended - Inactive");

    private final String description;

    VendorStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean canTransitionTo(VendorStatus newStatus) {
        return switch (this) {
            case PENDING -> newStatus == APPROVED || newStatus == REJECTED;
            case APPROVED -> newStatus == SUSPENDED;
            case REJECTED -> newStatus == PENDING; // Can re-apply
            case SUSPENDED -> newStatus == APPROVED || newStatus == PENDING;
        };
    }

    public boolean isActive() {
        return this == APPROVED;
    }
}