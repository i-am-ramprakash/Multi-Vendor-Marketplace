package com.marketplace.cart.application.dto;

import com.marketplace.cart.domain.entity.Cart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {

    private Long id;
    private Long userId;
    private String sessionId;
    private String status;
    private String currency;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal total;
    private String couponCode;
    private int itemCount;
    private List<CartItemResponse> items;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant expiresAt;

    public static CartResponse from(Cart cart) {
        return CartResponse.builder()
            .id(cart.getId())
            .userId(cart.getUserId())
            .sessionId(cart.getSessionId())
            .status(cart.getStatus().name())
            .currency(cart.getCurrency())
            .subtotal(cart.getSubtotal())
            .taxAmount(cart.getTaxAmount())
            .discountAmount(cart.getDiscountAmount())
            .total(cart.getTotal())
            .couponCode(cart.getCouponCode())
            .itemCount(cart.getItemCount())
            .items(cart.getItems().stream()
                .map(CartItemResponse::from)
                .collect(Collectors.toList()))
            .createdAt(cart.getCreatedAt())
            .updatedAt(cart.getUpdatedAt())
            .expiresAt(cart.getExpiresAt())
            .build();
    }
}