package com.marketplace.notification.infrastructure.persistence.mapper;

import com.marketplace.notification.domain.entity.NotificationAuditLog;
import com.marketplace.notification.infrastructure.persistence.entity.NotificationAuditLogJpaEntity;

import java.lang.reflect.Field;

public final class NotificationAuditLogPersistenceMapper {

    private NotificationAuditLogPersistenceMapper() {}

    public static NotificationAuditLogJpaEntity toJpaEntity(NotificationAuditLog domain) {
        if (domain == null) return null;

        NotificationAuditLogJpaEntity jpa = new NotificationAuditLogJpaEntity();
        jpa.setId(domain.getId());
        jpa.setNotificationId(domain.getNotificationId());
        jpa.setAction(domain.getAction());
        jpa.setDetails(domain.getDetails());
        jpa.setPerformedBy(domain.getPerformedBy());
        jpa.setCreatedAt(domain.getCreatedAt());

        return jpa;
    }

    public static NotificationAuditLog toDomain(NotificationAuditLogJpaEntity jpa) {
        if (jpa == null) return null;

        NotificationAuditLog auditLog = new NotificationAuditLog(
            jpa.getNotificationId(),
            jpa.getAction(),
            jpa.getDetails(),
            jpa.getPerformedBy()
        );
        setId(auditLog, jpa.getId());
        auditLog.setCreatedAt(jpa.getCreatedAt());

        return auditLog;
    }

    private static void setId(NotificationAuditLog auditLog, Long id) {
        try {
            Field field = NotificationAuditLog.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(auditLog, id);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot set notification audit log ID", e);
        }
    }
}