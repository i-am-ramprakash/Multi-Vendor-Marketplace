package com.marketplace.notification.application.usecase;

import com.marketplace.notification.application.dto.NotificationStatsResponse;
import com.marketplace.notification.domain.repository.NotificationRepository;
import com.marketplace.notification.domain.valueobject.NotificationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;

@Component
@RequiredArgsConstructor
public class GetNotificationStatsUseCase {

    private final NotificationRepository notificationRepository;

    @Transactional(readOnly = true)
    public NotificationStatsResponse execute() {
        long totalNotifications = notificationRepository.countByStatus(NotificationStatus.SENT) +
            notificationRepository.countByStatus(NotificationStatus.DELIVERED) +
            notificationRepository.countByStatus(NotificationStatus.FAILED) +
            notificationRepository.countByStatus(NotificationStatus.PENDING) +
            notificationRepository.countByStatus(NotificationStatus.RETRYING) +
            notificationRepository.countByStatus(NotificationStatus.DEAD_LETTER);

        long sentCount = notificationRepository.countByStatus(NotificationStatus.SENT);
        long deliveredCount = notificationRepository.countByStatus(NotificationStatus.DELIVERED);
        long failedCount = notificationRepository.countByStatus(NotificationStatus.FAILED);
        long pendingCount = notificationRepository.countByStatus(NotificationStatus.PENDING);
        long retryingCount = notificationRepository.countByStatus(NotificationStatus.RETRYING);
        long deadLetterCount = notificationRepository.countByStatus(NotificationStatus.DEAD_LETTER);

        // Calculate today, this week, this month counts
        Instant now = Instant.now();
        Instant startOfDay = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant startOfWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY))
            .atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant startOfMonth = LocalDate.now().withDayOfMonth(1)
            .atStartOfDay(ZoneId.systemDefault()).toInstant();

        long todayCount = notificationRepository.findByCreatedAtBetween(startOfDay, now).size();
        long thisWeekCount = notificationRepository.findByCreatedAtBetween(startOfWeek, now).size();
        long thisMonthCount = notificationRepository.findByCreatedAtBetween(startOfMonth, now).size();

        return NotificationStatsResponse.builder()
            .totalNotifications(totalNotifications)
            .sentCount(sentCount)
            .deliveredCount(deliveredCount)
            .failedCount(failedCount)
            .pendingCount(pendingCount)
            .retryingCount(retryingCount)
            .deadLetterCount(deadLetterCount)
            .todayCount(todayCount)
            .thisWeekCount(thisWeekCount)
            .thisMonthCount(thisMonthCount)
            .lastUpdated(now)
            .build();
    }
}