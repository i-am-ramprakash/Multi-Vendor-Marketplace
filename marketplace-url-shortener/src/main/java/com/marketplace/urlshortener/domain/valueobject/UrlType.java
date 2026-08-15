package com.marketplace.urlshortener.domain.valueobject;

import lombok.Getter;

@Getter
public enum UrlType {
    PRODUCT("Product", "Short link to a product page"),
    VENDOR_STORE("Vendor Store", "Short link to a vendor store"),
    CATEGORY("Category", "Short link to a category page"),
    CAMPAIGN("Campaign", "Short link to a campaign/promotion"),
    CUSTOM("Custom", "Custom short link");

    private final String displayName;
    private final String description;

    UrlType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
}