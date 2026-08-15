package com.marketplace.notification.application.usecase;

import com.marketplace.notification.application.dto.NotificationResponse;
import com.marketplace.notification.domain.entity.Notification;
import com.marketplace.notification.domain.entity.NotificationAuditLog;
import com.marketplace.notification.domain.exception.NotificationNotFoundException;
import com.marketplace.notification.domain.repository.NotificationAuditLogRepository;
import com.marketplace.notification.domain.repository.NotificationRepository;
import com.marketplace.notification.domain.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class RetryNotificationUseCase {

    private final NotificationRepository notificationRepository;
    private final NotificationAuditLogRepository auditLogRepository;
    private final EmailService emailService;

    @Transactional
    public NotificationResponse execute(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new NotificationNotFoundException(notificationId));

        if (!notification.canBeRetried()) {
            throw new IllegalStateException("Notification cannot be retried");
        }

        // Retry notification
        notification.retry();
        notification.setNextRetryAt(Instant.now().plusSeconds(30 * notification.getRetryCount()));

        Notification savedNotification = notificationRepository.save(notification);

        // Create audit log
        NotificationAuditLog auditLog = new NotificationAuditLog(
            savedNotification.getId(),
            "NOTIFICATION_RETRYING",
            "Retry attempt #" + savedNotification.getRetryCount(),
            null
        );
        auditLogRepository.save(auditLog);

        // Attempt to send
        try {
            emailService.sendEmail(
                savedNotification.getRecipientEmail(),
                savedNotification.getSubject(),
                savedNotification.getBody()
            );
            savedNotification.markSent("email", "0", "0");
        } catch (Exception e) {
            savedNotification.markFailed("Retry failed: " + e.getMessage());
        }

        notificationRepository.save(savedNotification);
        return NotificationResponse.from(savedNotification);
    }
}