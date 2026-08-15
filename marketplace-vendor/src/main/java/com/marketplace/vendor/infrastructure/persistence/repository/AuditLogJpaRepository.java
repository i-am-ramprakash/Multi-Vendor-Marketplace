package com.marketplace.vendor.infrastructure.persistence.repository;

import com.marketplace.vendor.infrastructure.persistence.entity.AuditLogJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface AuditLogJpaRepository extends JpaRepository<AuditLogJpaEntity, Long> {

    List<AuditLogJpaEntity> findByEntityTypeAndEntityId(String entityType, Long entityId);

    List<AuditLogJpaEntity> findByEntityTypeAndEntityIdOrderByCreatedAtDesc(String entityType, Long entityId);

    List<AuditLogJpaEntity> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<AuditLogJpaEntity> findByCreatedAtBetween(Instant start, Instant end);

    List<AuditLogJpaEntity> findTop100ByOrderByCreatedAtDesc();
}