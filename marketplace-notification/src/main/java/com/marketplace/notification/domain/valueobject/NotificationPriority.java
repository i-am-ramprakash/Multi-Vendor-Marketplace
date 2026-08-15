package com.marketplace.notification.domain.valueobject;

import lombok.Getter;

@Getter
public enum NotificationPriority {
    LOW("Low", "Low priority notification"),
    NORMAL("Normal", "Normal priority notification"),
    HIGH("High", "High priority notification"),
    URGENT("Urgent", "Urgent priority notification");

    private final String displayName;
    private final String description;

    NotificationPriority(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
}