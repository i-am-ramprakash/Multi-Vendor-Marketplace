package com.marketplace.vendor.domain.repository;

import com.marketplace.vendor.domain.entity.AuditLog;

import java.time.Instant;
import java.util.List;

public interface AuditLogRepository {

    AuditLog save(AuditLog auditLog);

    List<AuditLog> findByEntityTypeAndEntityId(String entityType, Long entityId);

    List<AuditLog> findByEntityTypeAndEntityIdOrderByCreatedAtDesc(String entityType, Long entityId);

    List<AuditLog> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<AuditLog> findByCreatedAtBetween(Instant start, Instant end);

    List<AuditLog> findTop100ByOrderByCreatedAtDesc();
}