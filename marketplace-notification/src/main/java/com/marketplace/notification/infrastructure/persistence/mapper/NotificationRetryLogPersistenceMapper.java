package com.marketplace.notification.infrastructure.persistence.mapper;

import com.marketplace.notification.domain.entity.NotificationRetryLog;
import com.marketplace.notification.infrastructure.persistence.entity.NotificationRetryLogJpaEntity;

import java.lang.reflect.Field;

public final class NotificationRetryLogPersistenceMapper {

    private NotificationRetryLogPersistenceMapper() {}

    public static NotificationRetryLogJpaEntity toJpaEntity(NotificationRetryLog domain) {
        if (domain == null) return null;

        NotificationRetryLogJpaEntity jpa = new NotificationRetryLogJpaEntity();
        jpa.setId(domain.getId());
        jpa.setNotificationId(domain.getNotificationId());
        jpa.setAttemptNumber(domain.getAttemptNumber());
        jpa.setStatus(domain.getStatus());
        jpa.setErrorMessage(domain.getErrorMessage());
        jpa.setDurationMs(domain.getDurationMs());
        jpa.setAttemptedAt(domain.getAttemptedAt());

        return jpa;
    }

    public static NotificationRetryLog toDomain(NotificationRetryLogJpaEntity jpa) {
        if (jpa == null) return null;

        NotificationRetryLog retryLog = new NotificationRetryLog(
            jpa.getNotificationId(),
            jpa.getAttemptNumber(),
            jpa.getStatus(),
            jpa.getErrorMessage(),
            jpa.getDurationMs()
        );
        setId(retryLog, jpa.getId());
        retryLog.setAttemptedAt(jpa.getAttemptedAt());

        return retryLog;
    }

    private static void setId(NotificationRetryLog retryLog, Long id) {
        try {
            Field field = NotificationRetryLog.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(retryLog, id);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot set notification retry log ID", e);
        }
    }
}