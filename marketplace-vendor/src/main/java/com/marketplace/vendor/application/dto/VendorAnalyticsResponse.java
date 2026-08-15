package com.marketplace.vendor.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Vendor analytics response")
public class VendorAnalyticsResponse {

    @Schema(description = "Vendor ID")
    private Long vendorId;

    @Schema(description = "Analytics period")
    private String period;

    @Schema(description = "Total orders")
    private Integer totalOrders;

    @Schema(description = "Total revenue")
    private BigDecimal totalRevenue;

    @Schema(description = "Total commission")
    private BigDecimal totalCommission;

    @Schema(description = "Vendor payout")
    private BigDecimal vendorPayout;

    @Schema(description = "Average order value")
    private BigDecimal averageOrderValue;

    @Schema(description = "Conversion rate")
    private BigDecimal conversionRate;

    @Schema(description = "Total page views")
    private Integer totalPageViews;

    @Schema(description = "Unique visitors")
    private Integer uniqueVisitors;

    @Schema(description = "Top products")
    private List<TopProduct> topProducts;

    @Schema(description = "Revenue by day")
    private List<RevenueByDay> revenueByDay;

    @Schema(description = "Order status breakdown")
    private List<OrderStatusBreakdown> orderStatusBreakdown;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Top product")
    public static class TopProduct {
        @Schema(example = "1")
        private Long productId;

        @Schema(example = "Classic T-Shirt")
        private String productName;

        @Schema(example = "25")
        private Integer quantitySold;

        @Schema(example = "2500.00")
        private BigDecimal revenue;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Revenue by day")
    public static class RevenueByDay {
        @Schema(example = "2024-01-15")
        private String date;

        @Schema(example = "2500.00")
        private BigDecimal revenue;

        @Schema(example = "25")
        private Integer orders;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Order status breakdown")
    public static class OrderStatusBreakdown {
        @Schema(example = "COMPLETED")
        private String status;

        @Schema(example = "50")
        private Integer count;

        @Schema(example = "60.0")
        private BigDecimal percentage;
    }
}