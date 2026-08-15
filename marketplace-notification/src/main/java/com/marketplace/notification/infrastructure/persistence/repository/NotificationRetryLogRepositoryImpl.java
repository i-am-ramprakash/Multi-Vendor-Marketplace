package com.marketplace.notification.infrastructure.persistence.repository;

import com.marketplace.notification.domain.entity.NotificationRetryLog;
import com.marketplace.notification.domain.repository.NotificationRetryLogRepository;
import com.marketplace.notification.infrastructure.persistence.entity.NotificationRetryLogJpaEntity;
import com.marketplace.notification.infrastructure.persistence.mapper.NotificationRetryLogPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class NotificationRetryLogRepositoryImpl implements NotificationRetryLogRepository {

    private final NotificationRetryLogJpaRepository jpaRepository;

    @Override
    public NotificationRetryLog save(NotificationRetryLog retryLog) {
        NotificationRetryLogJpaEntity jpa = NotificationRetryLogPersistenceMapper.toJpaEntity(retryLog);
        NotificationRetryLogJpaEntity saved = jpaRepository.save(jpa);
        return NotificationRetryLogPersistenceMapper.toDomain(saved);
    }

    @Override
    public List<NotificationRetryLog> findByNotificationId(Long notificationId) {
        return jpaRepository.findByNotificationId(notificationId).stream()
            .map(NotificationRetryLogPersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public long countByNotificationId(Long notificationId) {
        return jpaRepository.countByNotificationId(notificationId);
    }
}