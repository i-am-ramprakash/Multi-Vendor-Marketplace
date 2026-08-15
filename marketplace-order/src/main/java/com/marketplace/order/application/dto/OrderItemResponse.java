package com.marketplace.order.application.dto;

import com.marketplace.order.domain.entity.OrderItem;
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
public class OrderItemResponse {

    private Long id;
    private Long productId;
    private Long variantId;
    private Long vendorId;
    private String productName;
    private String variantName;
    private BigDecimal unitPrice;
    private int quantity;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal commissionRate;
    private BigDecimal commissionAmount;
    private BigDecimal vendorPayout;
    private String status;
    private String sku;
    private String imageUrl;
    private String notes;
    private Instant createdAt;

    public static OrderItemResponse from(OrderItem item) {
        return OrderItemResponse.builder()
            .id(item.getId())
            .productId(item.getProductId())
            .variantId(item.getVariantId())
            .vendorId(item.getVendorId())
            .productName(item.getProductName())
            .variantName(item.getVariantName())
            .unitPrice(item.getUnitPrice().getAmount())
            .quantity(item.getQuantity())
            .subtotal(item.getSubtotal().getAmount())
            .taxAmount(item.getTaxAmount().getAmount())
            .commissionRate(item.getCommissionRate().getAmount())
            .commissionAmount(item.getCommissionAmount().getAmount())
            .vendorPayout(item.getVendorPayout().getAmount())
            .status(item.getStatus().name())
            .sku(item.getSku())
            .imageUrl(item.getImageUrl())
            .notes(item.getNotes())
            .createdAt(item.getCreatedAt())
            .build();
    }
}