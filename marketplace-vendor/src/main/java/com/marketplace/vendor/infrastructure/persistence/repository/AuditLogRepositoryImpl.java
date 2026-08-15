package com.marketplace.vendor.infrastructure.persistence.repository;

import com.marketplace.vendor.domain.entity.AuditLog;
import com.marketplace.vendor.domain.repository.AuditLogRepository;
import com.marketplace.vendor.infrastructure.persistence.entity.AuditLogJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AuditLogRepositoryImpl implements AuditLogRepository {

    private final AuditLogJpaRepository jpaRepository;

    @Override
    public AuditLog save(AuditLog auditLog) {
        AuditLogJpaEntity jpa = toJpaEntity(auditLog);
        AuditLogJpaEntity saved = jpaRepository.save(jpa);
        return toDomain(saved);
    }

    @Override
    public List<AuditLog> findByEntityTypeAndEntityId(String entityType, Long entityId) {
        return jpaRepository.findByEntityTypeAndEntityId(entityType, entityId).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<AuditLog> findByEntityTypeAndEntityIdOrderByCreatedAtDesc(String entityType, Long entityId) {
        return jpaRepository.findByEntityTypeAndEntityIdOrderByCreatedAtDesc(entityType, entityId).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<AuditLog> findByUserIdOrderByCreatedAtDesc(Long userId) {
        return jpaRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<AuditLog> findByCreatedAtBetween(Instant start, Instant end) {
        return jpaRepository.findByCreatedAtBetween(start, end).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<AuditLog> findTop100ByOrderByCreatedAtDesc() {
        return jpaRepository.findTop100ByOrderByCreatedAtDesc().stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    private AuditLogJpaEntity toJpaEntity(AuditLog domain) {
        if (domain == null) return null;

        AuditLogJpaEntity jpa = new AuditLogJpaEntity();
        jpa.setId(domain.getId());
        jpa.setEntityType(domain.getEntityType());
        jpa.setEntityId(domain.getEntityId());
        jpa.setAction(domain.getAction());
        jpa.setOldValues(domain.getOldValues());
        jpa.setNewValues(domain.getNewValues());
        jpa.setChangedFields(domain.getChangedFields());
        jpa.setUserId(domain.getUserId());
        jpa.setIpAddress(domain.getIpAddress());
        jpa.setUserAgent(domain.getUserAgent());
        jpa.setCorrelationId(domain.getCorrelationId());
        jpa.setCreatedAt(domain.getCreatedAt());
        return jpa;
    }

    private AuditLog toDomain(AuditLogJpaEntity jpa) {
        if (jpa == null) return null;

        AuditLog domain = AuditLog.create(jpa.getEntityType(), jpa.getEntityId(), jpa.getAction(), jpa.getUserId());
        
        try {
            java.lang.reflect.Field idField = AuditLog.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(domain, jpa.getId());
        } catch (Exception e) {
            throw new IllegalStateException("Cannot set audit log ID", e);
        }

        domain.setOldValues(jpa.getOldValues());
        domain.setNewValues(jpa.getNewValues());
        domain.setChangedFields(jpa.getChangedFields());
        domain.setIpAddress(jpa.getIpAddress());
        domain.setUserAgent(jpa.getUserAgent());
        domain.setCorrelationId(jpa.getCorrelationId());
        domain.setCreatedAt(jpa.getCreatedAt());

        return domain;
    }
}