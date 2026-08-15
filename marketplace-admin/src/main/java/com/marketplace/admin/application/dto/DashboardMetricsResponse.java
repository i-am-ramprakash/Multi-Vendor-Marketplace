package com.marketplace.admin.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Dashboard metrics response")
public class DashboardMetricsResponse {

    @Schema(description = "Total registered users")
    private Long totalUsers;

    @Schema(description = "Total registered vendors")
    private Long totalVendors;

    @Schema(description = "Active vendors")
    private Long activeVendors;

    @Schema(description = "Pending vendor approvals")
    private Long pendingVendors;

    @Schema(description = "Total products")
    private Long totalProducts;

    @Schema(description = "Approved products")
    private Long approvedProducts;

    @Schema(description = "Pending product approvals")
    private Long pendingProducts;

    @Schema(description = "Total orders placed")
    private Long totalOrders;

    @Schema(description = "Pending orders")
    private Long pendingOrders;

    @Schema(description = "Completed orders")
    private Long completedOrders;

    @Schema(description = "Cancelled orders")
    private Long cancelledOrders;

    @Schema(description = "Total revenue from all orders")
    private BigDecimal totalRevenue;

    @Schema(description = "Total commission earned")
    private BigDecimal commissionRevenue;

    @Schema(description = "Average order value")
    private BigDecimal averageOrderValue;

    @Schema(description = "Current month revenue")
    private BigDecimal monthlyRevenue;

    @Schema(description = "Previous month revenue")
    private BigDecimal previousMonthRevenue;

    @Schema(description = "Revenue growth rate percentage")
    private Double revenueGrowthRate;

    @Schema(description = "New user registrations this month")
    private Long newUsersThisMonth;

    @Schema(description = "New orders this month")
    private Long newOrdersThisMonth;

    @Schema(description = "Metrics generation timestamp")
    private LocalDateTime generatedAt;
}