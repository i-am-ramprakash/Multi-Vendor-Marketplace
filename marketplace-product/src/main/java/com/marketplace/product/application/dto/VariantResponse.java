package com.marketplace.product.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Variant response")
public class VariantResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "Size: Large, Color: Red")
    private String name;

    @Schema(example = "TSH-L-RED")
    private String sku;

    @Schema(example = "1234567890124")
    private String barcode;

    @Schema(example = "29.99")
    private BigDecimal price;

    @Schema(example = "39.99")
    private BigDecimal compareAtPrice;

    @Schema(example = "15.00")
    private BigDecimal costPrice;

    @Schema(example = "0.25")
    private BigDecimal weight;

    @Schema(example = "30x20x5")
    private String dimensions;

    @Schema(example = "100")
    private Integer inventoryQuantity;

    @Schema(example = "10")
    private Integer lowStockThreshold;

    @Schema(example = "true")
    private Boolean trackInventory;

    @Schema(example = "false")
    private Boolean allowBackorder;

    @Schema(example = "https://example.com/image.jpg")
    private String imageUrl;

    @Schema(example = "0")
    private Integer position;

    @Schema(example = "true")
    private Boolean isActive;

    @Schema(example = "true")
    private Boolean inStock;

    @Schema(example = "false")
    private Boolean lowStock;

    @Schema(example = "25.00")
    private BigDecimal discountPercentage;

    public static VariantResponse from(com.marketplace.product.domain.entity.ProductVariant variant) {
        return VariantResponse.builder()
            .id(variant.getId())
            .name(variant.getName())
            .sku(variant.getSku() != null ? variant.getSku().getValue() : null)
            .barcode(variant.getBarcode())
            .price(variant.getPrice())
            .compareAtPrice(variant.getCompareAtPrice())
            .costPrice(variant.getCostPrice())
            .weight(variant.getWeight())
            .dimensions(variant.getDimensions())
            .inventoryQuantity(variant.getInventoryQuantity())
            .lowStockThreshold(variant.getLowStockThreshold())
            .trackInventory(variant.getTrackInventory())
            .allowBackorder(variant.getAllowBackorder())
            .imageUrl(variant.getImageUrl())
            .position(variant.getPosition())
            .isActive(variant.getIsActive())
            .inStock(variant.isInStock())
            .lowStock(variant.isLowStock())
            .discountPercentage(variant.getDiscountPercentage())
            .build();
    }
}