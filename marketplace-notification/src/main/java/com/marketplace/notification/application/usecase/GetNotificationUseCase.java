package com.marketplace.notification.application.usecase;

import com.marketplace.notification.application.dto.NotificationResponse;
import com.marketplace.notification.domain.entity.Notification;
import com.marketplace.notification.domain.exception.NotificationNotFoundException;
import com.marketplace.notification.domain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GetNotificationUseCase {

    private final NotificationRepository notificationRepository;

    @Transactional(readOnly = true)
    public NotificationResponse execute(Long id) {
        Notification notification = notificationRepository.findById(id)
            .orElseThrow(() -> new NotificationNotFoundException(id));
        return NotificationResponse.from(notification);
    }

    @Transactional(readOnly = true)
    public NotificationResponse executeByReferenceId(String referenceId) {
        Notification notification = notificationRepository.findByReferenceId(referenceId)
            .orElseThrow(() -> new NotificationNotFoundException(referenceId));
        return NotificationResponse.from(notification);
    }
}