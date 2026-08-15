package com.marketplace.order.domain.event;

import lombok.Getter;

@Getter
public class OrderCreatedEvent extends DomainEvent {

    private final Long orderId;
    private final String orderNumber;
    private final Long userId;
    private final java.math.BigDecimal totalAmount;
    private final String currency;

    public OrderCreatedEvent(Object source, Long orderId, String orderNumber, Long userId,
                            java.math.BigDecimal totalAmount, String currency) {
        super(source);
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.currency = currency;
    }
}