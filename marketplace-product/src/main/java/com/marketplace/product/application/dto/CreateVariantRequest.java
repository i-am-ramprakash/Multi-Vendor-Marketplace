package com.marketplace.product.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Create variant request")
public class CreateVariantRequest {

    @NotBlank(message = "Variant name is required")
    @Size(min = 1, max = 255, message = "Variant name must be between 1 and 255 characters")
    @Schema(example = "Size: Large, Color: Red", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @Schema(example = "29.99", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal price;

    @DecimalMin(value = "0.01", message = "Compare at price must be greater than 0")
    @Schema(example = "39.99")
    private BigDecimal compareAtPrice;

    @DecimalMin(value = "0.01", message = "Cost price must be greater than 0")
    @Schema(example = "15.00")
    private BigDecimal costPrice;

    @Size(max = 50, message = "SKU cannot exceed 50 characters")
    @Schema(example = "TSH-L-RED")
    private String sku;

    @Size(max = 100, message = "Barcode cannot exceed 100 characters")
    @Schema(example = "1234567890124")
    private String barcode;

    @DecimalMin(value = "0.001", message = "Weight must be greater than 0")
    @Schema(example = "0.25")
    private BigDecimal weight;

    @Size(max = 100, message = "Dimensions cannot exceed 100 characters")
    @Schema(example = "30x20x5")
    private String dimensions;

    @Min(value = 0, message = "Inventory quantity cannot be negative")
    @Schema(example = "100")
    private Integer inventoryQuantity;

    @Min(value = 0, message = "Low stock threshold cannot be negative")
    @Schema(example = "10")
    private Integer lowStockThreshold;

    @Schema(example = "true")
    private Boolean trackInventory;

    @Schema(example = "false")
    private Boolean allowBackorder;

    @Size(max = 500, message = "Image URL cannot exceed 500 characters")
    @Schema(example = "https://example.com/image.jpg")
    private String imageUrl;

    @Min(value = 0, message = "Position cannot be negative")
    @Schema(example = "0")
    private Integer position;

    @Schema(example = "true")
    private Boolean isActive;
}