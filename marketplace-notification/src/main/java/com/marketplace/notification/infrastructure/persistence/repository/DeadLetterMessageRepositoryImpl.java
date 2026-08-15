package com.marketplace.notification.infrastructure.persistence.repository;

import com.marketplace.notification.domain.entity.DeadLetterMessage;
import com.marketplace.notification.domain.repository.DeadLetterMessageRepository;
import com.marketplace.notification.infrastructure.persistence.entity.DeadLetterMessageJpaEntity;
import com.marketplace.notification.infrastructure.persistence.mapper.DeadLetterMessagePersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class DeadLetterMessageRepositoryImpl implements DeadLetterMessageRepository {

    private final DeadLetterMessageJpaRepository jpaRepository;

    @Override
    public DeadLetterMessage save(DeadLetterMessage deadLetterMessage) {
        DeadLetterMessageJpaEntity jpa = DeadLetterMessagePersistenceMapper.toJpaEntity(deadLetterMessage);
        DeadLetterMessageJpaEntity saved = jpaRepository.save(jpa);
        return DeadLetterMessagePersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<DeadLetterMessage> findById(Long id) {
        return jpaRepository.findById(id)
            .map(DeadLetterMessagePersistenceMapper::toDomain);
    }

    @Override
    public List<DeadLetterMessage> findByNotificationId(Long notificationId) {
        return jpaRepository.findByNotificationId(notificationId).stream()
            .map(DeadLetterMessagePersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<DeadLetterMessage> findByResolved(boolean resolved) {
        return jpaRepository.findByResolved(resolved).stream()
            .map(DeadLetterMessagePersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Page<DeadLetterMessage> findByResolved(boolean resolved, Pageable pageable) {
        return jpaRepository.findByResolved(resolved, pageable)
            .map(DeadLetterMessagePersistenceMapper::toDomain);
    }

    @Override
    public long countByResolved(boolean resolved) {
        return jpaRepository.countByResolved(resolved);
    }
}