package com.marketplace.urlshortener.domain.valueobject;

import lombok.Getter;

@Getter
public enum UrlStatus {
    ACTIVE("Active", "URL is active and accessible"),
    INACTIVE("Inactive", "URL is deactivated"),
    EXPIRED("Expired", "URL has expired"),
    BANNED("Banned", "URL has been banned");

    private final String displayName;
    private final String description;

    UrlStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public boolean canBeAccessed() {
        return this == ACTIVE;
    }
}