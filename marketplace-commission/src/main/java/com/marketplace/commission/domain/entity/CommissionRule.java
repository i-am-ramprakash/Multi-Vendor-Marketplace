package com.marketplace.commission.domain.entity;

import com.marketplace.commission.domain.valueobject.CommissionType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommissionRule {

    private Long id;
    private String name;
    private String description;
    private CommissionType type;
    private BigDecimal rate;
    private BigDecimal fixedAmount;
    private BigDecimal minOrderAmount;
    private BigDecimal maxCommissionAmount;
    private Long categoryId;
    private Long vendorId;
    private boolean isDefault;
    private boolean isActive;
    private int priority;
    private Instant effectiveFrom;
    private Instant effectiveTo;
    private Instant createdAt;
    private Instant updatedAt;

    public CommissionRule(String name, String description, CommissionType type, BigDecimal rate) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.rate = rate;
        this.fixedAmount = BigDecimal.ZERO;
        this.minOrderAmount = BigDecimal.ZERO;
        this.maxCommissionAmount = null;
        this.isDefault = false;
        this.isActive = true;
        this.priority = 0;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public BigDecimal calculateCommission(BigDecimal orderAmount) {
        if (!isActive) {
            return BigDecimal.ZERO;
        }

        if (orderAmount.compareTo(minOrderAmount) < 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal commission;
        switch (type) {
            case PERCENTAGE -> commission = orderAmount.multiply(rate).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            case FIXED -> commission = fixedAmount;
            case TIERED -> commission = calculateTieredCommission(orderAmount);
            default -> throw new IllegalStateException("Unknown commission type: " + type);
        }

        if (maxCommissionAmount != null && commission.compareTo(maxCommissionAmount) > 0) {
            commission = maxCommissionAmount;
        }

        return commission.max(BigDecimal.ZERO);
    }

    private BigDecimal calculateTieredCommission(BigDecimal orderAmount) {
        // Tiered commission implementation
        // Can be extended with tier configuration
        return orderAmount.multiply(rate).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }

    public boolean isApplicableToVendor(Long vendorId) {
        return this.vendorId == null || this.vendorId.equals(vendorId);
    }

    public boolean isApplicableToCategory(Long categoryId) {
        return this.categoryId == null || this.categoryId.equals(categoryId);
    }

    public boolean isCurrentlyEffective() {
        Instant now = Instant.now();
        boolean afterStart = effectiveFrom == null || !now.isBefore(effectiveFrom);
        boolean beforeEnd = effectiveTo == null || !now.isAfter(effectiveTo);
        return afterStart && beforeEnd;
    }

    public void activate() {
        this.isActive = true;
        this.updatedAt = Instant.now();
    }

    public void deactivate() {
        this.isActive = false;
        this.updatedAt = Instant.now();
    }
}