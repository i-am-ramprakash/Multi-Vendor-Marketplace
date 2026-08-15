package com.marketplace.commission.infrastructure.persistence.mapper;

import com.marketplace.commission.domain.entity.Settlement;
import com.marketplace.commission.domain.valueobject.Money;
import com.marketplace.commission.infrastructure.persistence.entity.SettlementJpaEntity;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class SettlementPersistenceMapper {

    private SettlementPersistenceMapper() {}

    public static SettlementJpaEntity toJpaEntity(Settlement domain) {
        if (domain == null) return null;

        SettlementJpaEntity jpa = new SettlementJpaEntity();
        jpa.setId(domain.getId());
        jpa.setSettlementNumber(domain.getSettlementNumber());
        jpa.setVendorId(domain.getVendorId());
        jpa.setStatus(domain.getStatus());
        jpa.setTotalAmount(domain.getTotalAmount().getAmount());
        jpa.setCommissionAmount(domain.getCommissionAmount().getAmount());
        jpa.setNetPayout(domain.getNetPayout().getAmount());
        jpa.setCurrency(domain.getCurrency());
        jpa.setRecordCount(domain.getRecordCount());
        jpa.setPeriodStart(domain.getPeriodStart());
        jpa.setPeriodEnd(domain.getPeriodEnd());
        jpa.setProcessedAt(domain.getProcessedAt());
        jpa.setCompletedAt(domain.getCompletedAt());
        jpa.setPaymentMethod(domain.getPaymentMethod());
        jpa.setPaymentReference(domain.getPaymentReference());
        jpa.setNotes(domain.getNotes());
        jpa.setFailureReason(domain.getFailureReason());
        jpa.setCommissionRecordIds(domain.getCommissionRecordIds().stream()
            .map(String::valueOf)
            .collect(Collectors.joining(",")));
        jpa.setCreatedAt(domain.getCreatedAt());
        jpa.setUpdatedAt(domain.getUpdatedAt());
        jpa.setVersion(domain.getVersion());

        return jpa;
    }

    public static Settlement toDomain(SettlementJpaEntity jpa) {
        if (jpa == null) return null;

        Settlement settlement = new Settlement(jpa.getVendorId(), jpa.getCurrency(), jpa.getPeriodStart(), jpa.getPeriodEnd());
        setId(settlement, jpa.getId());
        settlement.setSettlementNumber(jpa.getSettlementNumber());
        settlement.setStatus(jpa.getStatus());
        settlement.setTotalAmount(Money.of(jpa.getTotalAmount(), jpa.getCurrency()));
        settlement.setCommissionAmount(Money.of(jpa.getCommissionAmount(), jpa.getCurrency()));
        settlement.setNetPayout(Money.of(jpa.getNetPayout(), jpa.getCurrency()));
        settlement.setRecordCount(jpa.getRecordCount());
        settlement.setProcessedAt(jpa.getProcessedAt());
        settlement.setCompletedAt(jpa.getCompletedAt());
        settlement.setPaymentMethod(jpa.getPaymentMethod());
        settlement.setPaymentReference(jpa.getPaymentReference());
        settlement.setNotes(jpa.getNotes());
        settlement.setFailureReason(jpa.getFailureReason());
        settlement.setCreatedAt(jpa.getCreatedAt());
        settlement.setUpdatedAt(jpa.getUpdatedAt());
        settlement.setVersion(jpa.getVersion());

        if (jpa.getCommissionRecordIds() != null && !jpa.getCommissionRecordIds().isEmpty()) {
            List<Long> recordIds = Arrays.stream(jpa.getCommissionRecordIds().split(","))
                .map(String::trim)
                .map(Long::parseLong)
                .collect(Collectors.toList());
            for (Long recordId : recordIds) {
                settlement.getCommissionRecordIds().add(recordId);
            }
        }

        return settlement;
    }

    private static void setId(Settlement settlement, Long id) {
        try {
            Field field = Settlement.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(settlement, id);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot set settlement ID", e);
        }
    }
}