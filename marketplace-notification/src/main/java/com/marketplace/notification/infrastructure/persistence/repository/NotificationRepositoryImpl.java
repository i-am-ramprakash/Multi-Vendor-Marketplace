package com.marketplace.notification.infrastructure.persistence.repository;

import com.marketplace.notification.domain.entity.Notification;
import com.marketplace.notification.domain.repository.NotificationRepository;
import com.marketplace.notification.domain.valueobject.NotificationChannel;
import com.marketplace.notification.domain.valueobject.NotificationStatus;
import com.marketplace.notification.domain.valueobject.NotificationType;
import com.marketplace.notification.infrastructure.persistence.entity.NotificationJpaEntity;
import com.marketplace.notification.infrastructure.persistence.mapper.NotificationPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {

    private final NotificationJpaRepository jpaRepository;

    @Override
    public Notification save(Notification notification) {
        NotificationJpaEntity jpa = NotificationPersistenceMapper.toJpaEntity(notification);
        NotificationJpaEntity saved = jpaRepository.save(jpa);
        return NotificationPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return jpaRepository.findById(id)
            .map(NotificationPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Notification> findByReferenceId(String referenceId) {
        return jpaRepository.findByReferenceId(referenceId)
            .map(NotificationPersistenceMapper::toDomain);
    }

    @Override
    public List<Notification> findByRecipientId(Long recipientId) {
        return jpaRepository.findByRecipientId(recipientId).stream()
            .map(NotificationPersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Page<Notification> findByRecipientId(Long recipientId, Pageable pageable) {
        return jpaRepository.findByRecipientId(recipientId, pageable)
            .map(NotificationPersistenceMapper::toDomain);
    }

    @Override
    public List<Notification> findByStatus(NotificationStatus status) {
        return jpaRepository.findByStatus(status).stream()
            .map(NotificationPersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Page<Notification> findByStatus(NotificationStatus status, Pageable pageable) {
        return jpaRepository.findByStatus(status, pageable)
            .map(NotificationPersistenceMapper::toDomain);
    }

    @Override
    public List<Notification> findByType(NotificationType type) {
        return jpaRepository.findByType(type).stream()
            .map(NotificationPersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Notification> findByChannel(NotificationChannel channel) {
        return jpaRepository.findByChannel(channel).stream()
            .map(NotificationPersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Notification> findByRecipientIdAndStatus(Long recipientId, NotificationStatus status) {
        return jpaRepository.findByRecipientIdAndStatus(recipientId, status).stream()
            .map(NotificationPersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Page<Notification> findByRecipientIdAndStatus(Long recipientId, NotificationStatus status, Pageable pageable) {
        return jpaRepository.findByRecipientIdAndStatus(recipientId, status, pageable)
            .map(NotificationPersistenceMapper::toDomain);
    }

    @Override
    public List<Notification> findByCreatedAtBetween(Instant start, Instant end) {
        return jpaRepository.findByCreatedAtBetween(start, end).stream()
            .map(NotificationPersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Notification> findRetryableNotifications(Instant now) {
        return jpaRepository.findRetryableNotifications(now).stream()
            .map(NotificationPersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public long countByStatus(NotificationStatus status) {
        return jpaRepository.countByStatus(status);
    }

    @Override
    public long countByType(NotificationType type) {
        return jpaRepository.countByType(type);
    }

    @Override
    public long countByChannel(NotificationChannel channel) {
        return jpaRepository.countByChannel(channel);
    }
}