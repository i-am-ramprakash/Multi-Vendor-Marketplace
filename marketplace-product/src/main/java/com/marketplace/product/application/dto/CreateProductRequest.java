package com.marketplace.product.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Create product request")
public class CreateProductRequest {

    @NotNull(message = "Vendor ID is required")
    @Schema(example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long vendorId;

    @NotNull(message = "Category ID is required")
    @Schema(example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long categoryId;

    @NotBlank(message = "Product name is required")
    @Size(min = 1, max = 255, message = "Product name must be between 1 and 255 characters")
    @Schema(example = "Classic T-Shirt", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Size(max = 255, message = "Product slug cannot exceed 255 characters")
    @Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$", message = "Product slug can only contain lowercase letters, numbers, and hyphens")
    @Schema(example = "classic-t-shirt")
    private String slug;

    @Size(max = 10000, message = "Description cannot exceed 10000 characters")
    @Schema(example = "A comfortable classic t-shirt made from 100% cotton...")
    private String description;

    @Size(max = 500, message = "Short description cannot exceed 500 characters")
    @Schema(example = "Comfortable 100% cotton t-shirt")
    private String shortDescription;

    @NotNull(message = "Base price is required")
    @DecimalMin(value = "0.01", message = "Base price must be greater than 0")
    @Schema(example = "29.99", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal basePrice;

    @DecimalMin(value = "0.01", message = "Compare at price must be greater than 0")
    @Schema(example = "39.99")
    private BigDecimal compareAtPrice;

    @DecimalMin(value = "0.01", message = "Cost price must be greater than 0")
    @Schema(example = "15.00")
    private BigDecimal costPrice;

    @Size(max = 50, message = "SKU cannot exceed 50 characters")
    @Schema(example = "TSH-001")
    private String sku;

    @Size(max = 100, message = "Barcode cannot exceed 100 characters")
    @Schema(example = "1234567890123")
    private String barcode;

    @DecimalMin(value = "0.001", message = "Weight must be greater than 0")
    @Schema(example = "0.25")
    private BigDecimal weight;

    @Size(max = 100, message = "Dimensions cannot exceed 100 characters")
    @Schema(example = "30x20x5")
    private String dimensions;

    @Schema(example = "false")
    private Boolean isDigital;

    @Schema(example = "true")
    private Boolean requiresShipping;

    @Size(max = 50, message = "Tax class cannot exceed 50 characters")
    @Schema(example = "standard")
    private String taxClass;

    @Size(max = 255, message = "Meta title cannot exceed 255 characters")
    @Schema(example = "Classic T-Shirt - Fashion Paradise")
    private String metaTitle;

    @Size(max = 500, message = "Meta description cannot exceed 500 characters")
    @Schema(example = "Shop our classic t-shirt collection...")
    private String metaDescription;

    @Size(max = 500, message = "Meta keywords cannot exceed 500 characters")
    @Schema(example = "t-shirt, cotton, classic, fashion")
    private String metaKeywords;

    @Schema(description = "Product variants")
    private List<CreateVariantRequest> variants;

    @Schema(description = "Product images")
    private List<CreateImageRequest> images;
}