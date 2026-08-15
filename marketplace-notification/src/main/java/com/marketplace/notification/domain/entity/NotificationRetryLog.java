package com.marketplace.notification.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationRetryLog {

    private Long id;
    private Long notificationId;
    private int attemptNumber;
    private String status;
    private String errorMessage;
    private Instant attemptedAt;
    private long durationMs;

    public NotificationRetryLog(Long notificationId, int attemptNumber, String status,
                               String errorMessage, long durationMs) {
        this.notificationId = notificationId;
        this.attemptNumber = attemptNumber;
        this.status = status;
        this.errorMessage = errorMessage;
        this.durationMs = durationMs;
        this.attemptedAt = Instant.now();
    }
}