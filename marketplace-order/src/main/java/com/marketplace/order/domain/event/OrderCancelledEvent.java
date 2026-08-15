package com.marketplace.order.domain.event;

import lombok.Getter;

@Getter
public class OrderCancelledEvent extends DomainEvent {

    private final Long orderId;
    private final String orderNumber;
    private final Long userId;
    private final String reason;

    public OrderCancelledEvent(Object source, Long orderId, String orderNumber, Long userId, String reason) {
        super(source);
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.userId = userId;
        this.reason = reason;
    }
}