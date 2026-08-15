package com.marketplace.order.domain.event;

import com.marketplace.order.domain.valueobject.OrderStatus;
import lombok.Getter;

@Getter
public class OrderStatusChangedEvent extends DomainEvent {

    private final Long orderId;
    private final String orderNumber;
    private final OrderStatus oldStatus;
    private final OrderStatus newStatus;
    private final Long changedBy;

    public OrderStatusChangedEvent(Object source, Long orderId, String orderNumber,
                                   OrderStatus oldStatus, OrderStatus newStatus, Long changedBy) {
        super(source);
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.changedBy = changedBy;
    }
}