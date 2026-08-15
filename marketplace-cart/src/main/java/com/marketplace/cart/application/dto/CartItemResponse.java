package com.marketplace.cart.application.dto;

import com.marketplace.cart.domain.entity.CartItem;
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
public class CartItemResponse {

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
    private boolean availableForPurchase;
    private Instant addedAt;

    public static CartItemResponse from(CartItem item) {
        return CartItemResponse.builder()
            .id(item.getId())
            .productId(item.getProductId())
            .variantId(item.getVariantId())
            .productName(item.getProductName())
            .variantName(item.getVariantName())
            .unitPrice(item.getUnitPrice())
            .quantity(item.getQuantity())
            .subtotal(item.getSubtotal())
            .imageUrl(item.getImageUrl())
            .maxQuantity(item.getMaxQuantity())
            .inventoryAvailable(item.isInventoryAvailable())
            .availableForPurchase(item.isAvailableForPurchase())
            .addedAt(item.getAddedAt())
            .build();
    }
}