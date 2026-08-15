package com.marketplace.vendor.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuditLog {

    private Long id;
    private String entityType;
    private Long entityId;
    private String action;
    private String oldValues;
    private String newValues;
    private String changedFields;
    private Long userId;
    private String ipAddress;
    private String userAgent;
    private String correlationId;
    private Instant createdAt;

    public AuditLog(String entityType, Long entityId, String action, Long userId) {
        this.entityType = entityType;
        this.entityId = entityId;
        this.action = action;
        this.userId = userId;
    }

    public static AuditLog create(String entityType, Long entityId, String action, Long userId) {
        return new AuditLog(entityType, entityId, action, userId);
    }

    public AuditLog withOldValues(String oldValues) {
        this.oldValues = oldValues;
        return this;
    }

    public AuditLog withNewValues(String newValues) {
        this.newValues = newValues;
        return this;
    }

    public AuditLog withChangedFields(String changedFields) {
        this.changedFields = changedFields;
        return this;
    }

    public AuditLog withRequestInfo(String ipAddress, String userAgent) {
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        return this;
    }

    public AuditLog withCorrelationId(String correlationId) {
        this.correlationId = correlationId;
        return this;
    }
}
