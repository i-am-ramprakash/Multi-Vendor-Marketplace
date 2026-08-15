package com.marketplace.product.application.dto;

import com.marketplace.product.domain.valueobject.ProductStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Product response")
public class ProductResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "1")
    private Long vendorId;

    @Schema(example = "1")
    private Long categoryId;

    @Schema(example = "Electronics")
    private String categoryName;

    @Schema(example = "Classic T-Shirt")
    private String name;

    @Schema(example = "classic-t-shirt")
    private String slug;

    @Schema(example = "A comfortable classic t-shirt...")
    private String description;

    @Schema(example = "Comfortable 100% cotton t-shirt")
    private String shortDescription;

    @Schema(example = "29.99")
    private BigDecimal basePrice;

    @Schema(example = "39.99")
    private BigDecimal compareAtPrice;

    @Schema(example = "15.00")
    private BigDecimal costPrice;

    @Schema(example = "TSH-001")
    private String sku;

    @Schema(example = "1234567890123")
    private String barcode;

    @Schema(example = "0.25")
    private BigDecimal weight;

    @Schema(example = "30x20x5")
    private String dimensions;

    @Schema(example = "APPROVED")
    private ProductStatus status;

    @Schema(example = "false")
    private Boolean isFeatured;

    @Schema(example = "false")
    private Boolean isDigital;

    @Schema(example = "true")
    private Boolean requiresShipping;

    @Schema(example = "standard")
    private String taxClass;

    @Schema(example = "Classic T-Shirt - Fashion Paradise")
    private String metaTitle;

    @Schema(example = "Shop our classic t-shirt collection...")
    private String metaDescription;

    @Schema(example = "t-shirt, cotton, classic, fashion")
    private String metaKeywords;

    @Schema(example = "2024-01-15T10:30:00Z")
    private Instant approvedAt;

    @Schema(example = "Does not meet guidelines")
    private String rejectionReason;

    @Schema(example = "2024-01-15T10:30:00Z")
    private Instant publishedAt;

    @Schema(example = "50")
    private Integer totalSold;

    @Schema(example = "150")
    private Integer viewCount;

    @Schema(example = "4.50")
    private BigDecimal averageRating;

    @Schema(example = "25")
    private Integer reviewCount;

    @Schema(description = "Product variants")
    private List<VariantResponse> variants;

    @Schema(description = "Product images")
    private List<ImageResponse> images;

    @Schema(example = "2024-01-15T10:30:00Z")
    private Instant createdAt;

    @Schema(example = "2024-01-15T10:30:00Z")
    private Instant updatedAt;

    public static ProductResponse from(com.marketplace.product.domain.entity.Product product) {
        return ProductResponse.builder()
            .id(product.getId())
            .vendorId(product.getVendorId())
            .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
            .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
            .name(product.getName())
            .slug(product.getSlug() != null ? product.getSlug().getValue() : null)
            .description(product.getDescription())
            .shortDescription(product.getShortDescription())
            .basePrice(product.getBasePrice())
            .compareAtPrice(product.getCompareAtPrice())
            .costPrice(product.getCostPrice())
            .sku(product.getSku() != null ? product.getSku().getValue() : null)
            .barcode(product.getBarcode())
            .weight(product.getWeight())
            .dimensions(product.getDimensions())
            .status(product.getStatus())
            .isFeatured(product.getIsFeatured())
            .isDigital(product.getIsDigital())
            .requiresShipping(product.getRequiresShipping())
            .taxClass(product.getTaxClass())
            .metaTitle(product.getMetaTitle())
            .metaDescription(product.getMetaDescription())
            .metaKeywords(product.getMetaKeywords())
            .approvedAt(product.getApprovedAt())
            .rejectionReason(product.getRejectionReason())
            .publishedAt(product.getPublishedAt())
            .totalSold(product.getTotalSold())
            .viewCount(product.getViewCount())
            .averageRating(product.getAverageRating())
            .reviewCount(product.getReviewCount())
            .createdAt(product.getCreatedAt())
            .updatedAt(product.getUpdatedAt())
            .build();
    }
}