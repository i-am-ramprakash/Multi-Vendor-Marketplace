package com.marketplace.urlshortener.domain.valueobject;

import lombok.Getter;

@Getter
public enum ExpirationType {
    NONE("None", "No expiration"),
    MINUTES_5("5 Minutes", "Expires after 5 minutes"),
    MINUTES_30("30 Minutes", "Expires after 30 minutes"),
    HOURS_1("1 Hour", "Expires after 1 hour"),
    HOURS_24("24 Hours", "Expires after 24 hours"),
    DAYS_7("7 Days", "Expires after 7 days"),
    DAYS_30("30 Days", "Expires after 30 days"),
    DAYS_90("90 Days", "Expires after 90 days"),
    CUSTOM("Custom", "Custom expiration");

    private final String displayName;
    private final String description;

    ExpirationType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
}