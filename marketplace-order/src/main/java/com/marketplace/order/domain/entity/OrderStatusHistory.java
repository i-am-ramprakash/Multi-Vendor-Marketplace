package com.marketplace.order.domain.entity;

import com.marketplace.order.domain.valueobject.OrderStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderStatusHistory {

    private Long id;
    private OrderStatus status;
    private String notes;
    private Instant createdAt;

    public OrderStatusHistory(OrderStatus status, String notes) {
        this.status = status;
        this.notes = notes;
        this.createdAt = Instant.now();
    }
}