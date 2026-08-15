package com.marketplace.commission.application.dto;

import com.marketplace.commission.domain.entity.Settlement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettlementResponse {

    private Long id;
    private String settlementNumber;
    private Long vendorId;
    private String status;
    private BigDecimal totalAmount;
    private BigDecimal commissionAmount;
    private BigDecimal netPayout;
    private String currency;
    private int recordCount;
    private Instant periodStart;
    private Instant periodEnd;
    private Instant processedAt;
    private Instant completedAt;
    private String paymentMethod;
    private String paymentReference;
    private String notes;
    private String failureReason;
    private List<Long> commissionRecordIds;
    private Instant createdAt;
    private Instant updatedAt;

    public static SettlementResponse from(Settlement settlement) {
        return SettlementResponse.builder()
            .id(settlement.getId())
            .settlementNumber(settlement.getSettlementNumber())
            .vendorId(settlement.getVendorId())
            .status(settlement.getStatus().name())
            .totalAmount(settlement.getTotalAmount().getAmount())
            .commissionAmount(settlement.getCommissionAmount().getAmount())
            .netPayout(settlement.getNetPayout().getAmount())
            .currency(settlement.getCurrency())
            .recordCount(settlement.getRecordCount())
            .periodStart(settlement.getPeriodStart())
            .periodEnd(settlement.getPeriodEnd())
            .processedAt(settlement.getProcessedAt())
            .completedAt(settlement.getCompletedAt())
            .paymentMethod(settlement.getPaymentMethod())
            .paymentReference(settlement.getPaymentReference())
            .notes(settlement.getNotes())
            .failureReason(settlement.getFailureReason())
            .commissionRecordIds(settlement.getCommissionRecordIds())
            .createdAt(settlement.getCreatedAt())
            .updatedAt(settlement.getUpdatedAt())
            .build();
    }
}