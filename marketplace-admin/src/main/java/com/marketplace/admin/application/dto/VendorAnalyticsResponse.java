package com.marketplace.admin.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Vendor analytics response")
public class VendorAnalyticsResponse {

    @Schema(description = "Total registered vendors")
    private Long totalVendors;

    @Schema(description = "Active vendors")
    private Long activeVendors;

    @Schema(description = "Pending vendors")
    private Long pendingVendors;

    @Schema(description = "Suspended vendors")
    private Long suspendedVendors;

    @Schema(description = "Vendors registered this month")
    private Long newVendorsThisMonth;

    @Schema(description = "Vendor growth rate")
    private Double growthRate;

    @Schema(description = "Average vendor rating")
    private Double averageVendorRating;

    @Schema(description = "Total vendor revenue")
    private BigDecimal totalVendorRevenue;

    @Schema(description = "Average revenue per vendor")
    private BigDecimal averageRevenuePerVendor;

    @Schema(description = "Vendor performance list")
    private List<VendorPerformanceItem> topPerformers;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Vendor performance item")
    public static class VendorPerformanceItem {
        private Long vendorId;
        private String storeName;
        private BigDecimal revenue;
        private Long orders;
        private Double rating;
        private LocalDateTime joinedAt;
    }
}