package com.marketplace.admin.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Revenue analytics response")
public class RevenueAnalyticsResponse {

    @Schema(description = "Total revenue for the period")
    private BigDecimal totalRevenue;

    @Schema(description = "Total commission for the period")
    private BigDecimal totalCommission;

    @Schema(description = "Average daily revenue")
    private BigDecimal averageDailyRevenue;

    @Schema(description = "Revenue by day breakdown")
    private List<DailyRevenueItem> dailyRevenue;

    @Schema(description = "Revenue by category breakdown")
    private List<CategoryRevenueItem> categoryRevenue;

    @Schema(description = "Revenue by status breakdown")
    private List<OrderStatusItem> orderStatusBreakdown;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Daily revenue item")
    public static class DailyRevenueItem {
        private LocalDate date;
        private BigDecimal revenue;
        private BigDecimal commission;
        private Long orderCount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Category revenue item")
    public static class CategoryRevenueItem {
        private Long categoryId;
        private String categoryName;
        private BigDecimal revenue;
        private Long orderCount;
        private Double percentage;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Order status item")
    public static class OrderStatusItem {
        private String status;
        private Long count;
        private Double percentage;
    }
}