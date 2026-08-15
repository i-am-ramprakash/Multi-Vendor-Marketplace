package com.marketplace.order.application.dto;

import com.marketplace.order.domain.entity.Order;
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
public class OrderSummaryResponse {

    private Long id;
    private String orderNumber;
    private String status;
    private BigDecimal total;
    private String currency;
    private int itemCount;
    private Instant createdAt;

    public static OrderSummaryResponse from(Order order) {
        return OrderSummaryResponse.builder()
            .id(order.getId())
            .orderNumber(order.getOrderNumber().getValue())
            .status(order.getStatus().name())
            .total(order.getTotal().getAmount())
            .currency(order.getCurrency())
            .itemCount(order.getTotalItemCount())
            .createdAt(order.getCreatedAt())
            .build();
    }
}