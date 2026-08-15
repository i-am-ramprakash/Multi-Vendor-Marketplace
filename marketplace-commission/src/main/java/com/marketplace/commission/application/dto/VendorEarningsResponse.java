package com.marketplace.commission.application.dto;

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
public class VendorEarningsResponse {

    private Long vendorId;
    private BigDecimal totalSales;
    private BigDecimal totalCommission;
    private BigDecimal totalNetEarnings;
    private BigDecimal averageOrderValue;
    private int totalOrders;
    private int totalSettlements;
    private BigDecimal pendingPayout;
    private BigDecimal completedPayout;
    private Instant periodStart;
    private Instant periodEnd;
    private Instant lastSettlementAt;

    public static VendorEarningsResponse of(Long vendorId, BigDecimal totalSales, BigDecimal totalCommission,
                                           BigDecimal totalNetEarnings, int totalOrders) {
        return VendorEarningsResponse.builder()
            .vendorId(vendorId)
            .totalSales(totalSales)
            .totalCommission(totalCommission)
            .totalNetEarnings(totalNetEarnings)
            .averageOrderValue(totalOrders > 0 ? totalSales.divide(BigDecimal.valueOf(totalOrders), 2, java.math.RoundingMode.HALF_UP) : BigDecimal.ZERO)
            .totalOrders(totalOrders)
            .build();
    }
}