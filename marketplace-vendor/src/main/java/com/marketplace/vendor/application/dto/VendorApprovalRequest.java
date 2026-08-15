package com.marketplace.vendor.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Vendor approval request")
public class VendorApprovalRequest {

    @NotNull(message = "Vendor ID is required")
    @Schema(example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long vendorId;

    @Schema(example = "1")
    private Long approvedBy;

    @Schema(example = "5.00")
    private java.math.BigDecimal customCommissionRate;
}