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
@Schema(description = "Top vendor response")
public class TopVendorResponse {

    @Schema(description = "Vendor ID")
    private Long vendorId;

    @Schema(description = "Store name")
    private String storeName;

    @Schema(description = "Vendor owner name")
    private String ownerName;

    @Schema(description = "Total products listed")
    private Long totalProducts;

    @Schema(description = "Total orders received")
    private Long totalOrders;

    @Schema(description = "Total revenue generated")
    private BigDecimal totalRevenue;

    @Schema(description = "Commission paid")
    private BigDecimal commissionPaid;

    @Schema(description = "Average product rating")
    private Double averageRating;

    @Schema(description = "Rank in the list")
    private Integer rank;

    @Schema(description = "Vendor registration date")
    private LocalDateTime joinedAt;
}