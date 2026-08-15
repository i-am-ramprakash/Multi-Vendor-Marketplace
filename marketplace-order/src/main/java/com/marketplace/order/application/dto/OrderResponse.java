package com.marketplace.order.application.dto;

import com.marketplace.order.domain.entity.Order;
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
public class OrderResponse {

    private Long id;
    private String orderNumber;
    private Long userId;
    private String status;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal shippingAmount;
    private BigDecimal discountAmount;
    private BigDecimal commissionAmount;
    private BigDecimal total;
    private String currency;
    private String paymentMethod;
    private String paymentReference;
    private String shippingAddress;
    private String billingAddress;
    private String notes;
    private String cancellationReason;
    private int itemCount;
    private List<OrderItemResponse> items;
    private List<OrderStatusHistoryResponse> statusHistory;
    private Instant shippedAt;
    private Instant deliveredAt;
    private Instant cancelledAt;
    private Instant createdAt;
    private Instant updatedAt;

    public static OrderResponse from(Order order) {
        return OrderResponse.builder()
            .id(order.getId())
            .orderNumber(order.getOrderNumber().getValue())
            .userId(order.getUserId())
            .status(order.getStatus().name())
            .subtotal(order.getSubtotal().getAmount())
            .taxAmount(order.getTaxAmount().getAmount())
            .shippingAmount(order.getShippingAmount().getAmount())
            .discountAmount(order.getDiscountAmount().getAmount())
            .commissionAmount(order.getCommissionAmount().getAmount())
            .total(order.getTotal().getAmount())
            .currency(order.getCurrency())
            .paymentMethod(order.getPaymentMethod())
            .paymentReference(order.getPaymentReference())
            .shippingAddress(order.getShippingAddress())
            .billingAddress(order.getBillingAddress())
            .notes(order.getNotes())
            .cancellationReason(order.getCancellationReason())
            .itemCount(order.getTotalItemCount())
            .items(order.getItems().stream()
                .map(OrderItemResponse::from)
                .collect(Collectors.toList()))
            .statusHistory(order.getStatusHistory().stream()
                .map(OrderStatusHistoryResponse::from)
                .collect(Collectors.toList()))
            .shippedAt(order.getShippedAt())
            .deliveredAt(order.getDeliveredAt())
            .cancelledAt(order.getCancelledAt())
            .createdAt(order.getCreatedAt())
            .updatedAt(order.getUpdatedAt())
            .build();
    }
}