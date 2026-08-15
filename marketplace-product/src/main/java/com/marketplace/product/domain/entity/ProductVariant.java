package com.marketplace.product.domain.entity;

import com.marketplace.product.domain.valueobject.SKU;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductVariant {

    private Long id;
    private Product product;
    private String name;
    private SKU sku;
    private String barcode;
    private BigDecimal price;
    private BigDecimal compareAtPrice;
    private BigDecimal costPrice;
    private BigDecimal weight;
    private String dimensions;
    private Integer inventoryQuantity;
    private Integer lowStockThreshold;
    private Boolean trackInventory;
    private Boolean allowBackorder;
    private String imageUrl;
    private Integer position;
    private Boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;

    public ProductVariant(String name, BigDecimal price, SKU sku) {
        this.name = name;
        this.price = price;
        this.sku = sku;
        this.inventoryQuantity = 0;
        this.lowStockThreshold = 5;
        this.trackInventory = true;
        this.allowBackorder = false;
        this.position = 0;
        this.isActive = true;
    }

    public void updatePrice(BigDecimal price, BigDecimal compareAtPrice, BigDecimal costPrice) {
        if (price != null) this.price = price;
        if (compareAtPrice != null) this.compareAtPrice = compareAtPrice;
        if (costPrice != null) this.costPrice = costPrice;
    }

    public void updateInventory(int quantity) {
        this.inventoryQuantity = quantity;
    }

    public void incrementInventory(int quantity) {
        this.inventoryQuantity += quantity;
    }

    public void decrementInventory(int quantity) {
        if (this.trackInventory) {
            if (this.inventoryQuantity >= quantity) {
                this.inventoryQuantity -= quantity;
            } else if (this.allowBackorder) {
                this.inventoryQuantity -= quantity;
            } else {
                throw new IllegalStateException("Insufficient inventory for variant: " + this.name);
            }
        }
    }

    public boolean isInStock() {
        if (!trackInventory) {
            return true;
        }
        return inventoryQuantity > 0 || allowBackorder;
    }

    public boolean isLowStock() {
        if (!trackInventory) {
            return false;
        }
        return inventoryQuantity <= lowStockThreshold && inventoryQuantity > 0;
    }

    public boolean isOutOfStock() {
        if (!trackInventory) {
            return false;
        }
        return inventoryQuantity <= 0 && !allowBackorder;
    }

    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public boolean hasComparePrice() {
        return compareAtPrice != null && compareAtPrice.compareTo(price) > 0;
    }

    public BigDecimal getDiscountPercentage() {
        if (!hasComparePrice()) {
            return BigDecimal.ZERO;
        }
        return compareAtPrice.subtract(price)
            .divide(compareAtPrice, 4, BigDecimal.ROUND_HALF_UP)
            .multiply(new BigDecimal("100"))
            .setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
