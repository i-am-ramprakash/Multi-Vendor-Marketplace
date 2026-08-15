package com.marketplace.vendor.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Vendor dashboard response")
public class VendorDashboardResponse {

    @Schema(description = "Vendor information")
    private VendorResponse vendor;

    @Schema(description = "Sales summary for today")
    private SalesSummary todaySales;

    @Schema(description = "Sales summary for this week")
    private SalesSummary weekSales;

    @Schema(description = "Sales summary for this month")
    private SalesSummary monthSales;

    @Schema(description = "Recent orders")
    private List<OrderSummary> recentOrders;

    @Schema(description = "Top selling products")
    private List<ProductSummary> topProducts;

    @Schema(description = "Sales trend for last 30 days")
    private List<SalesTrend> salesTrend;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Sales summary")
    public static class SalesSummary {
        @Schema(example = "25")
        private Integer totalOrders;

        @Schema(example = "2500.00")
        private BigDecimal totalRevenue;

        @Schema(example = "250.00")
        private BigDecimal totalCommission;

        @Schema(example = "2250.00")
        private BigDecimal vendorPayout;

        @Schema(example = "100.00")
        private BigDecimal averageOrderValue;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Order summary")
    public static class OrderSummary {
        @Schema(example = "1")
        private Long orderId;

        @Schema(example = "ORD-2024-001")
        private String orderNumber;

        @Schema(example = "John Doe")
        private String customerName;

        @Schema(example = "150.00")
        private BigDecimal totalAmount;

        @Schema(example = "COMPLETED")
        private String status;

        @Schema(example = "2024-01-15T10:30:00Z")
        private Instant createdAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Product summary")
    public static class ProductSummary {
        @Schema(example = "1")
        private Long productId;

        @Schema(example = "Classic T-Shirt")
        private String productName;

        @Schema(example = "https://example.com/image.jpg")
        private String imageUrl;

        @Schema(example = "50")
        private Integer totalSold;

        @Schema(example = "750.00")
        private BigDecimal totalRevenue;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Sales trend")
    public static class SalesTrend {
        @Schema(example = "2024-01-15")
        private String date;

        @Schema(example = "25")
        private Integer orders;

        @Schema(example = "2500.00")
        private BigDecimal revenue;
    }
}