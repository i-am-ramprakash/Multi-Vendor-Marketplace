package com.marketplace.notification.infrastructure.persistence.repository;

import com.marketplace.notification.infrastructure.persistence.entity.NotificationAuditLogJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationAuditLogJpaRepository extends JpaRepository<NotificationAuditLogJpaEntity, Long> {

    List<NotificationAuditLogJpaEntity> findByNotificationId(Long notificationId);

    List<NotificationAuditLogJpaEntity> findByAction(String action);
}