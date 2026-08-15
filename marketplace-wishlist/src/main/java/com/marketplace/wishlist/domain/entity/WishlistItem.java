package com.marketplace.wishlist.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WishlistItem {

    private Long id;
    private Long productId;
    private Long variantId;
    private String productName;
    private String variantName;
    private BigDecimal unitPrice;
    private String imageUrl;
    private String vendorName;
    private Long vendorId;
    private boolean isAvailable;
    private Instant addedAt;
    private Instant updatedAt;

    public WishlistItem(Long productId, Long variantId, String productName, String variantName,
                       BigDecimal unitPrice, String imageUrl, String vendorName, Long vendorId) {
        this.productId = productId;
        this.variantId = variantId;
        this.productName = productName;
        this.variantName = variantName;
        this.unitPrice = unitPrice;
        this.imageUrl = imageUrl;
        this.vendorName = vendorName;
        this.vendorId = vendorId;
        this.isAvailable = true;
        this.addedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void updatePrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        this.updatedAt = Instant.now();
    }

    public void setAvailability(boolean available) {
        this.isAvailable = available;
        this.updatedAt = Instant.now();
    }

    public boolean hasVariant() {
        return variantId != null;
    }
}