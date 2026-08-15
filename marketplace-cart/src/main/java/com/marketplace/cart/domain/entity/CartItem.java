package com.marketplace.cart.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem {

    private Long id;
    private Long productId;
    private Long variantId;
    private String productName;
    private String variantName;
    private BigDecimal unitPrice;
    private int quantity;
    private BigDecimal subtotal;
    private String imageUrl;
    private int maxQuantity;
    private boolean inventoryAvailable;
    private Instant addedAt;
    private Instant updatedAt;

    public CartItem(Long productId, Long variantId, String productName, String variantName,
                   BigDecimal unitPrice, int quantity, String imageUrl) {
        this.productId = productId;
        this.variantId = variantId;
        this.productName = productName;
        this.variantName = variantName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.maxQuantity = 10;
        this.inventoryAvailable = true;
        this.addedAt = Instant.now();
        this.updatedAt = Instant.now();
        calculateSubtotal();
    }

    public void updateQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.quantity = quantity;
        this.updatedAt = Instant.now();
        calculateSubtotal();
    }

    public void updatePrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        this.updatedAt = Instant.now();
        calculateSubtotal();
    }

    public void setMaxQuantity(int maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public void setInventoryAvailable(boolean available) {
        this.inventoryAvailable = available;
    }

    public boolean isAvailableForPurchase() {
        return inventoryAvailable && quantity <= maxQuantity && quantity > 0;
    }

    public boolean hasVariant() {
        return variantId != null;
    }

    private void calculateSubtotal() {
        this.subtotal = this.unitPrice.multiply(BigDecimal.valueOf(this.quantity));
    }
}