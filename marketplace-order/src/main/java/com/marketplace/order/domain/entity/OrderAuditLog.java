package com.marketplace.order.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderAuditLog {

    private Long id;
    private String action;
    private Long performedBy;
    private String details;
    private Instant createdAt;

    public OrderAuditLog(String action, Long performedBy, String details) {
        this.action = action;
        this.performedBy = performedBy;
        this.details = details;
        this.createdAt = Instant.now();
    }
}