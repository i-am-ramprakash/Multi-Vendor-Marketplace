package com.marketplace.commission.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class CreateCommissionRuleRequest {

    @NotBlank(message = "Rule name is required")
    private String name;

    private String description;

    @NotBlank(message = "Commission type is required")
    private String type;

    @NotNull(message = "Rate is required")
    @Positive(message = "Rate must be positive")
    private BigDecimal rate;

    private BigDecimal fixedAmount;

    private BigDecimal minOrderAmount;

    private BigDecimal maxCommissionAmount;

    private Long categoryId;

    private Long vendorId;

    private boolean isDefault;

    private Instant effectiveFrom;

    private Instant effectiveTo;
}