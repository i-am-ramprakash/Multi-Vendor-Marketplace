package com.marketplace.vendor.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Vendor rejection request")
public class VendorRejectionRequest {

    @NotNull(message = "Vendor ID is required")
    @Schema(example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long vendorId;

    @Schema(example = "1")
    private Long rejectedBy;

    @NotBlank(message = "Rejection reason is required")
    @Size(min = 10, max = 1000, message = "Rejection reason must be between 10 and 1000 characters")
    @Schema(example = "Store description does not meet our guidelines...", requiredMode = Schema.RequiredMode.REQUIRED)
    private String rejectionReason;
}