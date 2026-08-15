package com.marketplace.admin.application.dto;

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
@Schema(description = "Product analytics response")
public class ProductAnalyticsResponse {

    @Schema(description = "Total products")
    private Long totalProducts;

    @Schema(description = "Approved products")
    private Long approvedProducts;

    @Schema(description = "Pending products")
    private Long pendingProducts;

    @Schema(description = "Rejected products")
    private Long rejectedProducts;

    @Schema(description = "Products added this month")
    private Long newProductsThisMonth;

    @Schema(description = "Product growth rate")
    private Double growthRate;

    @Schema(description = "Average product price")
    private BigDecimal averagePrice;

    @Schema(description = "Total product views")
    private Long totalViews;

    @Schema(description = "Total units sold")
    private Long totalUnitsSold;

    @Schema(description = "Products by category breakdown")
    private List<CategoryBreakdownItem> categoryBreakdown;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Category breakdown item")
    public static class CategoryBreakdownItem {
        private Long categoryId;
        private String categoryName;
        private Long productCount;
        private BigDecimal totalRevenue;
        private Double percentage;
    }
}