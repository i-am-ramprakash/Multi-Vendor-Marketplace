package com.marketplace.commission.application.dto;

import com.marketplace.commission.domain.entity.CommissionRule;
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
public class CommissionRuleResponse {

    private Long id;
    private String name;
    private String description;
    private String type;
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

    public static CommissionRuleResponse from(CommissionRule rule) {
        return CommissionRuleResponse.builder()
            .id(rule.getId())
            .name(rule.getName())
            .description(rule.getDescription())
            .type(rule.getType().name())
            .rate(rule.getRate())
            .fixedAmount(rule.getFixedAmount())
            .minOrderAmount(rule.getMinOrderAmount())
            .maxCommissionAmount(rule.getMaxCommissionAmount())
            .categoryId(rule.getCategoryId())
            .vendorId(rule.getVendorId())
            .isDefault(rule.isDefault())
            .isActive(rule.isActive())
            .priority(rule.getPriority())
            .effectiveFrom(rule.getEffectiveFrom())
            .effectiveTo(rule.getEffectiveTo())
            .createdAt(rule.getCreatedAt())
            .updatedAt(rule.getUpdatedAt())
            .build();
    }
}