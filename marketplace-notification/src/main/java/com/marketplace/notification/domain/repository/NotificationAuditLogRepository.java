package com.marketplace.notification.domain.repository;

import com.marketplace.notification.domain.entity.NotificationAuditLog;

import java.util.List;

public interface NotificationAuditLogRepository {

    NotificationAuditLog save(NotificationAuditLog auditLog);

    List<NotificationAuditLog> findByNotificationId(Long notificationId);

    List<NotificationAuditLog> findByAction(String action);
}