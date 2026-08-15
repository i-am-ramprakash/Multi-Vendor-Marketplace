package com.marketplace.wishlist.application.dto;

import com.marketplace.wishlist.domain.entity.WishlistItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishlistItemResponse {

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

    public static WishlistItemResponse from(WishlistItem item) {
        return WishlistItemResponse.builder()
            .id(item.getId())
            .productId(item.getProductId())
            .variantId(item.getVariantId())
            .productName(item.getProductName())
            .variantName(item.getVariantName())
            .unitPrice(item.getUnitPrice())
            .imageUrl(item.getImageUrl())
            .vendorName(item.getVendorName())
            .vendorId(item.getVendorId())
            .isAvailable(item.isAvailable())
            .addedAt(item.getAddedAt())
            .build();
    }
}