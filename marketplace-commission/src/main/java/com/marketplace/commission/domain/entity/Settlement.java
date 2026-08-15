package com.marketplace.commission.domain.entity;

import com.marketplace.commission.domain.valueobject.Money;
import com.marketplace.commission.domain.valueobject.SettlementStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Settlement {

    private Long id;
    private String settlementNumber;
    private Long vendorId;
    private SettlementStatus status;
    private Money totalAmount;
    private Money commissionAmount;
    private Money netPayout;
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
    private Instant createdAt;
    private Instant updatedAt;
    private Long version;

    private final List<Long> commissionRecordIds = new ArrayList<>();

    public Settlement(Long vendorId, String currency, Instant periodStart, Instant periodEnd) {
        this.settlementNumber = generateSettlementNumber();
        this.vendorId = vendorId;
        this.status = SettlementStatus.PENDING;
        this.currency = currency;
        this.totalAmount = Money.zero(currency);
        this.commissionAmount = Money.zero(currency);
        this.netPayout = Money.zero(currency);
        this.recordCount = 0;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.version = 0L;
    }

    public void addCommissionRecord(Long recordId, Money orderAmount, Money commission) {
        this.commissionRecordIds.add(recordId);
        this.totalAmount = this.totalAmount.add(orderAmount);
        this.commissionAmount = this.commissionAmount.add(commission);
        this.netPayout = this.totalAmount.subtract(this.commissionAmount);
        this.recordCount = this.commissionRecordIds.size();
        this.updatedAt = Instant.now();
    }

    public void process() {
        if (!status.canTransitionTo(SettlementStatus.PROCESSING)) {
            throw new IllegalStateException("Cannot process settlement in " + status + " status");
        }
        this.status = SettlementStatus.PROCESSING;
        this.processedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void complete(String paymentReference) {
        if (!status.canTransitionTo(SettlementStatus.COMPLETED)) {
            throw new IllegalStateException("Cannot complete settlement in " + status + " status");
        }
        this.status = SettlementStatus.COMPLETED;
        this.paymentReference = paymentReference;
        this.completedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void fail(String reason) {
        if (!status.canTransitionTo(SettlementStatus.FAILED)) {
            throw new IllegalStateException("Cannot fail settlement in " + status + " status");
        }
        this.status = SettlementStatus.FAILED;
        this.failureReason = reason;
        this.updatedAt = Instant.now();
    }

    public void cancel() {
        if (!status.canTransitionTo(SettlementStatus.CANCELLED)) {
            throw new IllegalStateException("Cannot cancel settlement in " + status + " status");
        }
        this.status = SettlementStatus.CANCELLED;
        this.updatedAt = Instant.now();
    }

    public void retry() {
        if (!status.canTransitionTo(SettlementStatus.PENDING)) {
            throw new IllegalStateException("Cannot retry settlement in " + status + " status");
        }
        this.status = SettlementStatus.PENDING;
        this.failureReason = null;
        this.updatedAt = Instant.now();
    }

    public boolean canBeProcessed() {
        return status == SettlementStatus.PENDING && recordCount > 0;
    }

    public boolean isActive() {
        return status != SettlementStatus.COMPLETED && status != SettlementStatus.CANCELLED;
    }

    private String generateSettlementNumber() {
        return "STL-" + System.currentTimeMillis();
    }
}