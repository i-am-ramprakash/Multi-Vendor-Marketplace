package com.marketplace.product.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Update inventory request")
public class UpdateInventoryRequest {

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    @Schema(example = "100", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer quantity;

    @Schema(example = "Restock from supplier")
    private String notes;

    @Schema(example = "MANUAL", description = "Reference type: ORDER, RETURN, MANUAL, RESTOCK, ADJUSTMENT")
    private String referenceType;

    @Schema(example = "1", description = "Reference ID")
    private Long referenceId;
}