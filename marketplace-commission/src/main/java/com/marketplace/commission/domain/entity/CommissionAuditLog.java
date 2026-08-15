package com.marketplace.commission.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommissionAuditLog {

    private Long id;
    private Long vendorId;
    private Long orderId;
    private Long commissionRecordId;
    private Long settlementId;
    private String action;
    private String details;
    private Long performedBy;
    private Instant createdAt;

    public CommissionAuditLog(String action, Long performedBy, String details) {
        this.action = action;
        this.performedBy = performedBy;
        this.details = details;
        this.createdAt = Instant.now();
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setCommissionRecordId(Long commissionRecordId) {
        this.commissionRecordId = commissionRecordId;
    }

    public void setSettlementId(Long settlementId) {
        this.settlementId = settlementId;
    }
}