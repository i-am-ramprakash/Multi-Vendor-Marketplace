package com.marketplace.notification.infrastructure.persistence.repository;

import com.marketplace.notification.domain.entity.NotificationAuditLog;
import com.marketplace.notification.domain.repository.NotificationAuditLogRepository;
import com.marketplace.notification.infrastructure.persistence.entity.NotificationAuditLogJpaEntity;
import com.marketplace.notification.infrastructure.persistence.mapper.NotificationAuditLogPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class NotificationAuditLogRepositoryImpl implements NotificationAuditLogRepository {

    private final NotificationAuditLogJpaRepository jpaRepository;

    @Override
    public NotificationAuditLog save(NotificationAuditLog auditLog) {
        NotificationAuditLogJpaEntity jpa = NotificationAuditLogPersistenceMapper.toJpaEntity(auditLog);
        NotificationAuditLogJpaEntity saved = jpaRepository.save(jpa);
        return NotificationAuditLogPersistenceMapper.toDomain(saved);
    }

    @Override
    public List<NotificationAuditLog> findByNotificationId(Long notificationId) {
        return jpaRepository.findByNotificationId(notificationId).stream()
            .map(NotificationAuditLogPersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<NotificationAuditLog> findByAction(String action) {
        return jpaRepository.findByAction(action).stream()
            .map(NotificationAuditLogPersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }
}