package com.marketplace.cart.application.dto;

import com.marketplace.cart.domain.entity.Cart;
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
public class CartSummaryResponse {

    private Long id;
    private int itemCount;
    private BigDecimal subtotal;
    private BigDecimal total;
    private String currency;
    private Instant updatedAt;

    public static CartSummaryResponse from(Cart cart) {
        return CartSummaryResponse.builder()
            .id(cart.getId())
            .itemCount(cart.getItemCount())
            .subtotal(cart.getSubtotal())
            .total(cart.getTotal())
            .currency(cart.getCurrency())
            .updatedAt(cart.getUpdatedAt())
            .build();
    }

    public static CartSummaryResponse from(CartResponse response) {
        return CartSummaryResponse.builder()
            .id(response.getId())
            .itemCount(response.getItemCount())
            .subtotal(response.getSubtotal())
            .total(response.getTotal())
            .currency(response.getCurrency())
            .updatedAt(response.getUpdatedAt())
            .build();
    }
}