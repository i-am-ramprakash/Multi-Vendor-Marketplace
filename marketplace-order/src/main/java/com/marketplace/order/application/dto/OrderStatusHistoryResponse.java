package com.marketplace.order.application.dto;

import com.marketplace.order.domain.entity.OrderStatusHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusHistoryResponse {

    private Long id;
    private String status;
    private String notes;
    private Instant createdAt;

    public static OrderStatusHistoryResponse from(OrderStatusHistory history) {
        return OrderStatusHistoryResponse.builder()
            .id(history.getId())
            .status(history.getStatus().name())
            .notes(history.getNotes())
            .createdAt(history.getCreatedAt())
            .build();
    }
}