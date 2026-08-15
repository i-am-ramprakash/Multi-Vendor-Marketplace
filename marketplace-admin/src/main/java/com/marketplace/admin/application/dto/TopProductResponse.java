package com.marketplace.admin.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Top product response")
public class TopProductResponse {

    @Schema(description = "Product ID")
    private Long productId;

    @Schema(description = "Product name")
    private String productName;

    @Schema(description = "Category name")
    private String categoryName;

    @Schema(description = "Vendor store name")
    private String vendorName;

    @Schema(description = "Total units sold")
    private Long totalSold;

    @Schema(description = "Total revenue generated")
    private BigDecimal totalRevenue;

    @Schema(description = "Average selling price")
    private BigDecimal averagePrice;

    @Schema(description = "Total reviews count")
    private Integer totalReviews;

    @Schema(description = "Average rating")
    private Double averageRating;

    @Schema(description = "Rank in the list")
    private Integer rank;
}