package com.marketplace.admin.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardMetrics {
    private Long totalUsers;
    private Long totalVendors;
    private Long activeVendors;
    private Long pendingVendors;
    private Long totalProducts;
    private Long approvedProducts;
    private Long pendingProducts;
    private Long totalOrders;
    private Long pendingOrders;
    private Long completedOrders;
    private Long cancelledOrders;
    private BigDecimal totalRevenue;
    private BigDecimal commissionRevenue;
    private BigDecimal averageOrderValue;
    private BigDecimal monthlyRevenue;
    private BigDecimal previousMonthRevenue;
    private Double revenueGrowthRate;
    private Long newUsersThisMonth;
    private Long newOrdersThisMonth;
    private LocalDateTime generatedAt;
}