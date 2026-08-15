package com.marketplace.notification.application.usecase;

import com.marketplace.notification.application.dto.NotificationListResponse;
import com.marketplace.notification.application.dto.NotificationResponse;
import com.marketplace.notification.domain.entity.Notification;
import com.marketplace.notification.domain.repository.NotificationRepository;
import com.marketplace.notification.domain.valueobject.NotificationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetUserNotificationsUseCase {

    private final NotificationRepository notificationRepository;

    @Transactional(readOnly = true)
    public NotificationListResponse execute(Long userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Notification> notifications = notificationRepository.findByRecipientId(userId, pageRequest);

        List<NotificationResponse> content = notifications.getContent().stream()
            .map(NotificationResponse::from)
            .collect(Collectors.toList());

        return NotificationListResponse.builder()
            .notifications(content)
            .totalElements(notifications.getTotalElements())
            .totalPages(notifications.getTotalPages())
            .page(notifications.getNumber())
            .size(notifications.getSize())
            .build();
    }

    @Transactional(readOnly = true)
    public NotificationListResponse execute(Long userId, String status, int page, int size) {
        NotificationStatus notificationStatus = NotificationStatus.valueOf(status);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Notification> notifications = notificationRepository.findByRecipientIdAndStatus(userId, notificationStatus, pageRequest);

        List<NotificationResponse> content = notifications.getContent().stream()
            .map(NotificationResponse::from)
            .collect(Collectors.toList());

        return NotificationListResponse.builder()
            .notifications(content)
            .totalElements(notifications.getTotalElements())
            .totalPages(notifications.getTotalPages())
            .page(notifications.getNumber())
            .size(notifications.getSize())
            .build();
    }
}