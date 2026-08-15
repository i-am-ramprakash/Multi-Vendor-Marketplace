package com.marketplace.commission.infrastructure.persistence.mapper;

import com.marketplace.commission.domain.entity.CommissionRecord;
import com.marketplace.commission.domain.valueobject.Money;
import com.marketplace.commission.infrastructure.persistence.entity.CommissionRecordJpaEntity;

import java.lang.reflect.Field;

public final class CommissionRecordPersistenceMapper {

    private CommissionRecordPersistenceMapper() {}

    public static CommissionRecordJpaEntity toJpaEntity(CommissionRecord domain) {
        if (domain == null) return null;

        CommissionRecordJpaEntity jpa = new CommissionRecordJpaEntity();
        jpa.setId(domain.getId());
        jpa.setOrderId(domain.getOrderId());
        jpa.setOrderItemId(domain.getOrderItemId());
        jpa.setVendorId(domain.getVendorId());
        jpa.setCommissionRuleId(domain.getCommissionRuleId());
        jpa.setOrderAmount(domain.getOrderAmount().getAmount());
        jpa.setCommissionAmount(domain.getCommissionAmount().getAmount());
        jpa.setVendorPayout(domain.getVendorPayout().getAmount());
        jpa.setCommissionRate(domain.getCommissionRate());
        jpa.setCurrency(domain.getCurrency());
        jpa.setSettled(domain.isSettled());
        jpa.setSettledAt(domain.getSettledAt());
        jpa.setSettlementId(domain.getSettlementId());
        jpa.setCreatedAt(domain.getCreatedAt());
        jpa.setUpdatedAt(domain.getUpdatedAt());

        return jpa;
    }

    public static CommissionRecord toDomain(CommissionRecordJpaEntity jpa) {
        if (jpa == null) return null;

        CommissionRecord record = new CommissionRecord(
            jpa.getOrderId(),
            jpa.getOrderItemId(),
            jpa.getVendorId(),
            jpa.getCommissionRuleId(),
            jpa.getOrderAmount(),
            jpa.getCommissionRate(),
            jpa.getCurrency()
        );
        setId(record, jpa.getId());
        record.setCommissionAmount(Money.of(jpa.getCommissionAmount(), jpa.getCurrency()));
        record.setVendorPayout(Money.of(jpa.getVendorPayout(), jpa.getCurrency()));
        record.setSettled(jpa.isSettled());
        record.setSettledAt(jpa.getSettledAt());
        record.setSettlementId(jpa.getSettlementId());
        record.setCreatedAt(jpa.getCreatedAt());
        record.setUpdatedAt(jpa.getUpdatedAt());

        return record;
    }

    private static void setId(CommissionRecord record, Long id) {
        try {
            Field field = CommissionRecord.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(record, id);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot set commission record ID", e);
        }
    }
}