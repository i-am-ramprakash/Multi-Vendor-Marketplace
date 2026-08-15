package com.marketplace.notification.domain.event.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderShippedEvent {

    private String eventId;
    private Long orderId;
    private String orderNumber;
    private Long userId;
    private String email;
    private String trackingNumber;
    private String carrier;
    private Instant estimatedDelivery;
    private Instant occurredOn;
}