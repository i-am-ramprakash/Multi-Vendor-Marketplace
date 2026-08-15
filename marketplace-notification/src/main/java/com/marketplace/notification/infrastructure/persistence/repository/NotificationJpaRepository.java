package com.marketplace.notification.infrastructure.persistence.repository;

import com.marketplace.notification.domain.valueobject.NotificationChannel;
import com.marketplace.notification.domain.valueobject.NotificationStatus;
import com.marketplace.notification.domain.valueobject.NotificationType;
import com.marketplace.notification.infrastructure.persistence.entity.NotificationJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationJpaRepository extends JpaRepository<NotificationJpaEntity, Long> {

    Optional<NotificationJpaEntity> findByReferenceId(String referenceId);

    List<NotificationJpaEntity> findByRecipientId(Long recipientId);

    Page<NotificationJpaEntity> findByRecipientId(Long recipientId, Pageable pageable);

    List<NotificationJpaEntity> findByStatus(NotificationStatus status);

    Page<NotificationJpaEntity> findByStatus(NotificationStatus status, Pageable pageable);

    List<NotificationJpaEntity> findByType(NotificationType type);

    List<NotificationJpaEntity> findByChannel(NotificationChannel channel);

    List<NotificationJpaEntity> findByRecipientIdAndStatus(Long recipientId, NotificationStatus status);

    Page<NotificationJpaEntity> findByRecipientIdAndStatus(Long recipientId, NotificationStatus status, Pageable pageable);

    List<NotificationJpaEntity> findByCreatedAtBetween(Instant start, Instant end);

    @Query("SELECT n FROM NotificationJpaEntity n WHERE n.status IN ('FAILED', 'RETRYING') AND n.retryCount < n.maxRetries AND (n.nextRetryAt IS NULL OR n.nextRetryAt <= :now)")
    List<NotificationJpaEntity> findRetryableNotifications(@Param("now") Instant now);

    long countByStatus(NotificationStatus status);

    long countByType(NotificationType type);

    long countByChannel(NotificationChannel channel);
}