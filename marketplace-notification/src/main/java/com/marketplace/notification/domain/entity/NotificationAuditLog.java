package com.marketplace.notification.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationAuditLog {

    private Long id;
    private Long notificationId;
    private String action;
    private String details;
    private Long performedBy;
    private Instant createdAt;

    public NotificationAuditLog(Long notificationId, String action, String details, Long performedBy) {
        this.notificationId = notificationId;
        this.action = action;
        this.details = details;
        this.performedBy = performedBy;
        this.createdAt = Instant.now();
    }
}