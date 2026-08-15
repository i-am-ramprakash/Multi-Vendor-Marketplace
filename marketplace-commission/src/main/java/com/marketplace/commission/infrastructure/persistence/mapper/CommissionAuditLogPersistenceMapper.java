package com.marketplace.commission.infrastructure.persistence.mapper;

import com.marketplace.commission.domain.entity.CommissionAuditLog;
import com.marketplace.commission.infrastructure.persistence.entity.CommissionAuditLogJpaEntity;

import java.lang.reflect.Field;

public final class CommissionAuditLogPersistenceMapper {

    private CommissionAuditLogPersistenceMapper() {}

    public static CommissionAuditLogJpaEntity toJpaEntity(CommissionAuditLog domain) {
        if (domain == null) return null;

        CommissionAuditLogJpaEntity jpa = new CommissionAuditLogJpaEntity();
        jpa.setId(domain.getId());
        jpa.setVendorId(domain.getVendorId());
        jpa.setOrderId(domain.getOrderId());
        jpa.setCommissionRecordId(domain.getCommissionRecordId());
        jpa.setSettlementId(domain.getSettlementId());
        jpa.setAction(domain.getAction());
        jpa.setDetails(domain.getDetails());
        jpa.setPerformedBy(domain.getPerformedBy());
        jpa.setCreatedAt(domain.getCreatedAt());

        return jpa;
    }

    public static CommissionAuditLog toDomain(CommissionAuditLogJpaEntity jpa) {
        if (jpa == null) return null;

        CommissionAuditLog auditLog = new CommissionAuditLog(jpa.getAction(), jpa.getPerformedBy(), jpa.getDetails());
        setId(auditLog, jpa.getId());
        auditLog.setVendorId(jpa.getVendorId());
        auditLog.setOrderId(jpa.getOrderId());
        auditLog.setCommissionRecordId(jpa.getCommissionRecordId());
        auditLog.setSettlementId(jpa.getSettlementId());
        auditLog.setCreatedAt(jpa.getCreatedAt());

        return auditLog;
    }

    private static void setId(CommissionAuditLog auditLog, Long id) {
        try {
            Field field = CommissionAuditLog.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(auditLog, id);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot set commission audit log ID", e);
        }
    }
}