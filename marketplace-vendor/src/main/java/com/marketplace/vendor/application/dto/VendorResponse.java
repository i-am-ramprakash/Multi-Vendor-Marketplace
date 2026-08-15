package com.marketplace.vendor.application.dto;

import com.marketplace.vendor.domain.valueobject.VendorStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Vendor response")
public class VendorResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "1")
    private Long userId;

    @Schema(example = "Fashion Paradise")
    private String storeName;

    @Schema(example = "fashion-paradise")
    private String storeSlug;

    @Schema(example = "We offer the latest fashion trends...")
    private String storeDescription;

    @Schema(example = "https://example.com/logo.png")
    private String storeLogoUrl;

    @Schema(example = "https://example.com/banner.png")
    private String storeBannerUrl;

    @Schema(example = "vendor@example.com")
    private String contactEmail;

    @Schema(example = "+1234567890")
    private String contactPhone;

    @Schema(example = "123 Fashion Street, Suite 100, New York, NY, USA 10001")
    private String fullAddress;

    @Schema(example = "10.00")
    private BigDecimal commissionRate;

    @Schema(example = "APPROVED")
    private VendorStatus status;

    @Schema(example = "10")
    private Integer totalProducts;

    @Schema(example = "150")
    private Integer totalOrders;

    @Schema(example = "15000.00")
    private BigDecimal totalRevenue;

    @Schema(example = "2024-01-15T10:30:00Z")
    private Instant createdAt;

    @Schema(example = "2024-01-20T14:45:00Z")
    private Instant updatedAt;

    @Schema(example = "2024-01-22T09:00:00Z")
    private Instant approvedAt;

    @Schema(example = "Does not meet quality standards")
    private String rejectionReason;

    @Schema(example = "Violation of terms of service")
    private String suspensionReason;


    public static VendorResponse from(com.marketplace.vendor.domain.entity.Vendor vendor) {
        return VendorResponse.builder()
            .id(vendor.getId())
            .userId(vendor.getUserId())
            .storeName(vendor.getStoreName())
            .storeSlug(vendor.getStoreSlug().getValue())
            .storeDescription(vendor.getStoreDescription())
            .storeLogoUrl(vendor.getStoreLogoUrl())
            .storeBannerUrl(vendor.getStoreBannerUrl())
            .contactEmail(vendor.getContactEmail())
            .contactPhone(vendor.getContactPhone())
            .fullAddress(vendor.getFullAddress())
            .commissionRate(vendor.getCommissionRate())
            .status(vendor.getStatus())
            .totalProducts(vendor.getTotalProducts())
            .totalOrders(vendor.getTotalOrders())
            .totalRevenue(vendor.getTotalRevenue())
            .createdAt(vendor.getCreatedAt())
            .updatedAt(vendor.getUpdatedAt())
            .build();
    }
}