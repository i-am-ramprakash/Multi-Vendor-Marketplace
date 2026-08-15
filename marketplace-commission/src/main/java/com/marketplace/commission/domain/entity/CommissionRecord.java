package com.marketplace.commission.domain.entity;

import com.marketplace.commission.domain.valueobject.Money;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommissionRecord {

    private Long id;
    private Long orderId;
    private Long orderItemId;
    private Long vendorId;
    private Long commissionRuleId;
    private Money orderAmount;
    private Money commissionAmount;
    private Money vendorPayout;
    private BigDecimal commissionRate;
    private String currency;
    private boolean isSettled;
    private Instant settledAt;
    private Long settlementId;
    private Instant createdAt;
    private Instant updatedAt;

    public CommissionRecord(Long orderId, Long orderItemId, Long vendorId, Long commissionRuleId,
                           BigDecimal orderAmount, BigDecimal commissionRate, String currency) {
        this.orderId = orderId;
        this.orderItemId = orderItemId;
        this.vendorId = vendorId;
        this.commissionRuleId = commissionRuleId;
        this.orderAmount = Money.of(orderAmount, currency);
        this.commissionRate = commissionRate;
        this.currency = currency;
        this.isSettled = false;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();

        calculateAmounts();
    }

    public void calculateAmounts() {
        this.commissionAmount = this.orderAmount.percentage(this.commissionRate);
        this.vendorPayout = this.orderAmount.subtract(this.commissionAmount);
    }

    public void markAsSettled(Long settlementId) {
        this.isSettled = true;
        this.settledAt = Instant.now();
        this.settlementId = settlementId;
        this.updatedAt = Instant.now();
    }

    public boolean canBeSettled() {
        return !isSettled;
    }
}