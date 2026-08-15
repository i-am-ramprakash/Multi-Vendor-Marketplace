package com.marketplace.notification.infrastructure.persistence.repository;

import com.marketplace.notification.infrastructure.persistence.entity.NotificationRetryLogJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRetryLogJpaRepository extends JpaRepository<NotificationRetryLogJpaEntity, Long> {

    List<NotificationRetryLogJpaEntity> findByNotificationId(Long notificationId);

    long countByNotificationId(Long notificationId);
}