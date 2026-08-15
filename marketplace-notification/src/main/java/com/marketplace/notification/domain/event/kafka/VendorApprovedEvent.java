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
public class VendorApprovedEvent {

    private String eventId;
    private Long vendorId;
    private Long userId;
    private String vendorName;
    private String storeName;
    private String email;
    private Instant occurredOn;
}