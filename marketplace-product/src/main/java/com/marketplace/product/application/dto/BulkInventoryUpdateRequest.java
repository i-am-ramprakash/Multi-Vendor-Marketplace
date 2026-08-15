package com.marketplace.product.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Bulk inventory update request")
public class BulkInventoryUpdateRequest {

    @NotEmpty(message = "At least one inventory update is required")
    @Valid
    @Schema(description = "List of inventory updates", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<InventoryUpdateItem> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Inventory update item")
    public static class InventoryUpdateItem {

        @Schema(example = "1", description = "Variant ID", requiredMode = Schema.RequiredMode.REQUIRED)
        private Long variantId;

        @Schema(example = "100", description = "New quantity", requiredMode = Schema.RequiredMode.REQUIRED)
        private Integer quantity;

        @Schema(example = "Restock from supplier")
        private String notes;
    }
}