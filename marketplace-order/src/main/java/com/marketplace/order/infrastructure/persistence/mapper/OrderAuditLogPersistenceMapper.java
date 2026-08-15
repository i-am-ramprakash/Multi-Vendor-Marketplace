package com.marketplace.order.infrastructure.persistence.mapper;

import com.marketplace.order.domain.entity.OrderAuditLog;
import com.marketplace.order.infrastructure.persistence.entity.OrderAuditLogJpaEntity;

import java.lang.reflect.Field;

public final class OrderAuditLogPersistenceMapper {

    private OrderAuditLogPersistenceMapper() {}

    public static OrderAuditLogJpaEntity toJpaEntity(OrderAuditLog domain) {
        if (domain == null) return null;

        OrderAuditLogJpaEntity jpa = new OrderAuditLogJpaEntity();
        jpa.setId(domain.getId());
        jpa.setAction(domain.getAction());
        jpa.setPerformedBy(domain.getPerformedBy());
        jpa.setDetails(domain.getDetails());
        jpa.setCreatedAt(domain.getCreatedAt());

        return jpa;
    }

    public static OrderAuditLog toDomain(OrderAuditLogJpaEntity jpa) {
        if (jpa == null) return null;

        OrderAuditLog auditLog = new OrderAuditLog(jpa.getAction(), jpa.getPerformedBy(), jpa.getDetails());
        setId(auditLog, jpa.getId());
        auditLog.setCreatedAt(jpa.getCreatedAt());

        return auditLog;
    }

    private static void setId(OrderAuditLog auditLog, Long id) {
        try {
            Field field = OrderAuditLog.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(auditLog, id);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot set order audit log ID", e);
        }
    }
}