package com.marketplace.commission.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyRevenueResponse {

    private int year;
    private int month;
    private BigDecimal totalSales;
    private BigDecimal totalCommission;
    private BigDecimal totalNetPayout;
    private int totalOrders;
    private int totalVendors;
    private BigDecimal averageCommissionRate;
    private List<DailyRevenue> dailyRevenues;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyRevenue {
        private int day;
        private BigDecimal sales;
        private BigDecimal commission;
        private int orders;
    }
}