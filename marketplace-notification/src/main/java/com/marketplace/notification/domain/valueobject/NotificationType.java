package com.marketplace.notification.domain.valueobject;

import lombok.Getter;

@Getter
public enum NotificationType {
    USER_REGISTERED("User Registered", "Notification when a new user registers"),
    VENDOR_APPROVED("Vendor Approved", "Notification when a vendor is approved"),
    PRODUCT_APPROVED("Product Approved", "Notification when a product is approved"),
    ORDER_CREATED("Order Created", "Notification when an order is created"),
    ORDER_SHIPPED("Order Shipped", "Notification when an order is shipped"),
    ORDER_DELIVERED("Order Delivered", "Notification when an order is delivered"),
    CUSTOM("Custom", "Custom notification");

    private final String displayName;
    private final String description;

    NotificationType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
}