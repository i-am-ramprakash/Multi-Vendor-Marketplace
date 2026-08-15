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
public class ProductApprovedEvent {

    private String eventId;
    private Long productId;
    private Long vendorId;
    private Long userId;
    private String productName;
    private String vendorName;
    private String email;
    private BigDecimal price;
    private String currency;
    private Instant occurredOn;
}