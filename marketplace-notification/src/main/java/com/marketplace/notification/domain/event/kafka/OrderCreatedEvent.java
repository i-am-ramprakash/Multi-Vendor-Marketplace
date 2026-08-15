package com.marketplace.notification.domain.event.kafka;

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
public class OrderCreatedEvent {

    private String eventId;
    private Long orderId;
    private String orderNumber;
    private Long userId;
    private String email;
    private BigDecimal totalAmount;
    private String currency;
    private int itemCount;
    private Instant occurredOn;
}