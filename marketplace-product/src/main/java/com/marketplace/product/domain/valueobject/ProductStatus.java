package com.marketplace.product.domain.valueobject;

public enum ProductStatus {
    DRAFT("Draft - Not published"),
    PENDING_APPROVAL("Pending admin approval"),
    APPROVED("Approved - Published"),
    REJECTED("Rejected - Not approved");

    private final String description;

    ProductStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean canTransitionTo(ProductStatus newStatus) {
        return switch (this) {
            case DRAFT -> newStatus == PENDING_APPROVAL || newStatus == APPROVED;
            case PENDING_APPROVAL -> newStatus == APPROVED || newStatus == REJECTED || newStatus == DRAFT;
            case APPROVED -> newStatus == DRAFT || newStatus == REJECTED;
            case REJECTED -> newStatus == DRAFT || newStatus == PENDING_APPROVAL;
        };
    }

    public boolean isPublished() {
        return this == APPROVED;
    }

    public boolean canBeEdited() {
        return this == DRAFT || this == REJECTED;
    }
}