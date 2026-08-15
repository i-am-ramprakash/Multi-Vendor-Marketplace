package com.marketplace.product.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Product approval response")
public class ProductApprovalResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "1")
    private Long productId;

    @Schema(example = "Classic T-Shirt")
    private String productName;

    @Schema(example = "1")
    private Long vendorId;

    @Schema(example = "NEW_PRODUCT")
    private String requestType;

    @Schema(example = "PENDING")
    private String status;

    @Schema(example = "This product meets all guidelines")
    private String adminNotes;

    @Schema(example = "Please add more product details")
    private String vendorNotes;

    @Schema(example = "2024-01-15T10:30:00Z")
    private Instant createdAt;

    @Schema(example = "2024-01-15T10:30:00Z")
    private Instant reviewedAt;

    public static ProductApprovalResponse from(com.marketplace.product.domain.entity.ProductApprovalRequest request) {
        return ProductApprovalResponse.builder()
            .id(request.getId())
            .productId(request.getProduct() != null ? request.getProduct().getId() : null)
            .productName(request.getProduct() != null ? request.getProduct().getName() : null)
            .vendorId(request.getVendorId())
            .requestType(request.getRequestType().name())
            .status(request.getStatus().name())
            .adminNotes(request.getAdminNotes())
            .vendorNotes(request.getVendorNotes())
            .createdAt(request.getCreatedAt())
            .reviewedAt(request.getReviewedAt())
            .build();
    }
}