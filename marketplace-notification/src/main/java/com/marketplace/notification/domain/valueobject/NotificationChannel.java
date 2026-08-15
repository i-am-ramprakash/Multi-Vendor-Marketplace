package com.marketplace.notification.domain.valueobject;

import lombok.Getter;

@Getter
public enum NotificationChannel {
    EMAIL("Email", "Email notification"),
    SMS("SMS", "SMS notification"),
    PUSH("Push", "Push notification"),
    IN_APP("In-App", "In-app notification"),
    WEBHOOK("Webhook", "Webhook notification");

    private final String displayName;
    private final String description;

    NotificationChannel(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
}