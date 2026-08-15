package com.marketplace.notification.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationStatsResponse {

    private long totalNotifications;
    private long sentCount;
    private long deliveredCount;
    private long failedCount;
    private long pendingCount;
    private long retryingCount;
    private long deadLetterCount;
    private long todayCount;
    private long thisWeekCount;
    private long thisMonthCount;
    private Instant lastUpdated;
}