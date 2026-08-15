package com.marketplace.notification.domain.repository;

import com.marketplace.notification.domain.entity.Notification;
import com.marketplace.notification.domain.valueobject.NotificationChannel;
import com.marketplace.notification.domain.valueobject.NotificationStatus;
import com.marketplace.notification.domain.valueobject.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository {

    Notification save(Notification notification);

    Optional<Notification> findById(Long id);

    Optional<Notification> findByReferenceId(String referenceId);

    List<Notification> findByRecipientId(Long recipientId);

    Page<Notification> findByRecipientId(Long recipientId, Pageable pageable);

    List<Notification> findByStatus(NotificationStatus status);

    Page<Notification> findByStatus(NotificationStatus status, Pageable pageable);

    List<Notification> findByType(NotificationType type);

    List<Notification> findByChannel(NotificationChannel channel);

    List<Notification> findByRecipientIdAndStatus(Long recipientId, NotificationStatus status);

    Page<Notification> findByRecipientIdAndStatus(Long recipientId, NotificationStatus status, Pageable pageable);

    List<Notification> findByCreatedAtBetween(Instant start, Instant end);

    List<Notification> findRetryableNotifications(Instant now);

    long countByStatus(NotificationStatus status);

    long countByType(NotificationType type);

    long countByChannel(NotificationChannel channel);
}