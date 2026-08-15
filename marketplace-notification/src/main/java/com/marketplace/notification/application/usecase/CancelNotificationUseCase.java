package com.marketplace.notification.application.usecase;

import com.marketplace.notification.application.dto.NotificationResponse;
import com.marketplace.notification.domain.entity.Notification;
import com.marketplace.notification.domain.entity.NotificationAuditLog;
import com.marketplace.notification.domain.exception.NotificationNotFoundException;
import com.marketplace.notification.domain.repository.NotificationAuditLogRepository;
import com.marketplace.notification.domain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CancelNotificationUseCase {

    private final NotificationRepository notificationRepository;
    private final NotificationAuditLogRepository auditLogRepository;

    @Transactional
    public NotificationResponse execute(Long notificationId, Long performedBy) {
        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new NotificationNotFoundException(notificationId));

        notification.cancel();

        Notification savedNotification = notificationRepository.save(notification);

        // Create audit log
        NotificationAuditLog auditLog = new NotificationAuditLog(
            savedNotification.getId(),
            "NOTIFICATION_CANCELLED",
            "Notification cancelled by user",
            performedBy
        );
        auditLogRepository.save(auditLog);

        return NotificationResponse.from(savedNotification);
    }
}