package com.marketplace.notification.infrastructure.service;

import com.marketplace.notification.domain.entity.Notification;
import com.marketplace.notification.domain.entity.NotificationAuditLog;
import com.marketplace.notification.domain.entity.NotificationRetryLog;
import com.marketplace.notification.domain.repository.NotificationAuditLogRepository;
import com.marketplace.notification.domain.repository.NotificationRepository;
import com.marketplace.notification.domain.repository.NotificationRetryLogRepository;
import com.marketplace.notification.domain.service.EmailService;
import com.marketplace.notification.domain.service.KafkaProducerService;
import com.marketplace.notification.domain.valueobject.NotificationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RetryMechanismService {

    private final NotificationRepository notificationRepository;
    private final NotificationRetryLogRepository retryLogRepository;
    private final NotificationAuditLogRepository auditLogRepository;
    private final EmailService emailService;
    private final KafkaProducerService kafkaProducerService;

    @Scheduled(fixedDelay = 60000) // Run every minute
    @Transactional
    public void processRetryableNotifications() {
        log.info("Processing retryable notifications...");

        List<Notification> retryableNotifications = notificationRepository.findRetryableNotifications(Instant.now());

        for (Notification notification : retryableNotifications) {
            try {
                processRetry(notification);
            } catch (Exception e) {
                log.error("Failed to retry notification {}: {}", notification.getId(), e.getMessage(), e);
            }
        }

        log.info("Processed {} retryable notifications", retryableNotifications.size());
    }

    private void processRetry(Notification notification) {
        long startTime = System.currentTimeMillis();

        try {
            // Retry sending based on channel
            switch (notification.getChannel()) {
                case EMAIL -> {
                    emailService.sendEmail(
                        notification.getRecipientEmail(),
                        notification.getSubject(),
                        notification.getBody()
                    );
                }
                case SMS -> {
                    // SMS implementation would go here
                    log.info("SMS notification sent to: {}", notification.getRecipientPhone());
                }
                case PUSH -> {
                    // Push notification implementation would go here
                    log.info("Push notification sent to user: {}", notification.getRecipientId());
                }
                default -> {
                    log.warn("Unsupported notification channel: {}", notification.getChannel());
                }
            }

            long duration = System.currentTimeMillis() - startTime;

            // Log successful retry
            NotificationRetryLog retryLog = new NotificationRetryLog(
                notification.getId(),
                notification.getRetryCount() + 1,
                "SUCCESS",
                null,
                duration
            );
            retryLogRepository.save(retryLog);

            // Update notification status
            notification.markSent("email", "0", "0");

            // Create audit log
            NotificationAuditLog auditLog = new NotificationAuditLog(
                notification.getId(),
                "NOTIFICATION_RETRY_SUCCESS",
                "Retry attempt #" + (notification.getRetryCount() + 1) + " succeeded",
                null
            );
            auditLogRepository.save(auditLog);

            log.info("Notification {} retried successfully", notification.getId());

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;

            // Log failed retry
            NotificationRetryLog retryLog = new NotificationRetryLog(
                notification.getId(),
                notification.getRetryCount() + 1,
                "FAILED",
                e.getMessage(),
                duration
            );
            retryLogRepository.save(retryLog);

            // Update notification status
            notification.markFailed("Retry failed: " + e.getMessage());

            // Create audit log
            NotificationAuditLog auditLog = new NotificationAuditLog(
                notification.getId(),
                "NOTIFICATION_RETRY_FAILED",
                "Retry attempt #" + (notification.getRetryCount() + 1) + " failed: " + e.getMessage(),
                null
            );
            auditLogRepository.save(auditLog);

            // Check if max retries reached
            if (!notification.canBeRetried()) {
                log.warn("Notification {} reached max retries, moving to dead letter queue", notification.getId());
                // Dead letter handling would be done in a separate service
            }

            throw new RuntimeException("Retry failed for notification: " + notification.getId(), e);
        }

        notificationRepository.save(notification);
    }
}