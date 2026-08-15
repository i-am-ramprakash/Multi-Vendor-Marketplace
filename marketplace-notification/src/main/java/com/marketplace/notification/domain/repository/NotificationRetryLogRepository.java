package com.marketplace.notification.domain.repository;

import com.marketplace.notification.domain.entity.NotificationRetryLog;

import java.util.List;

public interface NotificationRetryLogRepository {

    NotificationRetryLog save(NotificationRetryLog retryLog);

    List<NotificationRetryLog> findByNotificationId(Long notificationId);

    long countByNotificationId(Long notificationId);
}