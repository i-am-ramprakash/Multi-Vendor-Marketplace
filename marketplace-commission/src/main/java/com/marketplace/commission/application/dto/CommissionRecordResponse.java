package com.marketplace.commission.application.dto;

import com.marketplace.commission.domain.entity.CommissionRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommissionRecordResponse {

    private Long id;
    private Long orderId;
    private Long orderItemId;
    private Long vendorId;
    private Long commissionRuleId;
    private BigDecimal orderAmount;
    private BigDecimal commissionAmount;
    private BigDecimal vendorPayout;
    private BigDecimal commissionRate;
    private String currency;
    private boolean isSettled;
    private Instant settledAt;
    private Long settlementId;
    private Instant createdAt;

    public static CommissionRecordResponse from(CommissionRecord record) {
        return CommissionRecordResponse.builder()
            .id(record.getId())
            .orderId(record.getOrderId())
            .orderItemId(record.getOrderItemId())
            .vendorId(record.getVendorId())
            .commissionRuleId(record.getCommissionRuleId())
            .orderAmount(record.getOrderAmount().getAmount())
            .commissionAmount(record.getCommissionAmount().getAmount())
            .vendorPayout(record.getVendorPayout().getAmount())
            .commissionRate(record.getCommissionRate())
            .currency(record.getCurrency())
            .isSettled(record.isSettled())
            .settledAt(record.getSettledAt())
            .settlementId(record.getSettlementId())
            .createdAt(record.getCreatedAt())
            .build();
    }
}