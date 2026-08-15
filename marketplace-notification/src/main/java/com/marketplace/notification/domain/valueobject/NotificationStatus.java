package com.marketplace.notification.domain.valueobject;

import lombok.Getter;

@Getter
public enum NotificationStatus {
    PENDING("Pending", "Notification is pending"),
    QUEUED("Queued", "Notification is queued for processing"),
    PROCESSING("Processing", "Notification is being processed"),
    SENT("Sent", "Notification has been sent"),
    DELIVERED("Delivered", "Notification has been delivered"),
    FAILED("Failed", "Notification failed to send"),
    RETRYING("Retrying", "Notification is being retried"),
    DEAD_LETTER("Dead Letter", "Notification moved to dead letter queue"),
    CANCELLED("Cancelled", "Notification was cancelled");

    private final String displayName;
    private final String description;

    NotificationStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public boolean canTransitionTo(NotificationStatus newStatus) {
        return switch (this) {
            case PENDING -> newStatus == QUEUED || newStatus == CANCELLED;
            case QUEUED -> newStatus == PROCESSING || newStatus == CANCELLED;
            case PROCESSING -> newStatus == SENT || newStatus == FAILED;
            case SENT -> newStatus == DELIVERED || newStatus == FAILED;
            case FAILED -> newStatus == RETRYING || newStatus == DEAD_LETTER || newStatus == CANCELLED;
            case RETRYING -> newStatus == PROCESSING || newStatus == DEAD_LETTER || newStatus == CANCELLED;
            case DEAD_LETTER -> false;
            case CANCELLED -> false;
            case DELIVERED -> false;
        };
    }
}