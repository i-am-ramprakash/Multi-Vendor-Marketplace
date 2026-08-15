package com.marketplace.product.domain.entity;

import com.marketplace.product.domain.valueobject.ProductSlug;
import com.marketplace.product.domain.valueobject.ProductStatus;
import com.marketplace.product.domain.valueobject.SKU;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    private Long id;
    private Long vendorId;
    private Category category;
    private String name;
    private ProductSlug slug;
    private String description;
    private String shortDescription;
    private BigDecimal basePrice;
    private BigDecimal compareAtPrice;
    private BigDecimal costPrice;
    private SKU sku;
    private String barcode;
    private BigDecimal weight;
    private String dimensions;
    private ProductStatus status;
    private Boolean isFeatured;
    private Boolean isDigital;
    private Boolean requiresShipping;
    private String taxClass;
    private String metaTitle;
    private String metaDescription;
    private String metaKeywords;
    private Instant approvedAt;
    private Long approvedBy;
    private String rejectionReason;
    private Instant publishedAt;
    private Integer totalSold;
    private Integer viewCount;
    private BigDecimal averageRating;
    private Integer reviewCount;
    private List<ProductVariant> variants = new ArrayList<>();
    private List<ProductImage> images = new ArrayList<>();
    private Instant createdAt;
    private Instant updatedAt;
    private Long version;

    public Product(Long vendorId, Category category, String name, ProductSlug slug, BigDecimal basePrice) {
        this.vendorId = vendorId;
        this.category = category;
        this.name = name;
        this.slug = slug;
        this.basePrice = basePrice;
        this.status = ProductStatus.DRAFT;
        this.isFeatured = false;
        this.isDigital = false;
        this.requiresShipping = true;
        this.totalSold = 0;
        this.viewCount = 0;
        this.averageRating = BigDecimal.ZERO;
        this.reviewCount = 0;
    }

    public void submitForApproval() {
        if (!this.status.canTransitionTo(ProductStatus.PENDING_APPROVAL)) {
            throw new IllegalStateException("Cannot submit product in " + this.status + " status for approval");
        }
        this.status = ProductStatus.PENDING_APPROVAL;
    }

    public void approve(Long approvedBy) {
        if (!this.status.canTransitionTo(ProductStatus.APPROVED)) {
            throw new IllegalStateException("Cannot approve product in " + this.status + " status");
        }
        this.status = ProductStatus.APPROVED;
        this.approvedAt = Instant.now();
        this.approvedBy = approvedBy;
        this.publishedAt = Instant.now();
        this.rejectionReason = null;
    }

    public void reject(Long rejectedBy, String reason) {
        if (!this.status.canTransitionTo(ProductStatus.REJECTED)) {
            throw new IllegalStateException("Cannot reject product in " + this.status + " status");
        }
        this.status = ProductStatus.REJECTED;
        this.rejectionReason = reason;
    }

    public void unpublish() {
        if (!this.status.canTransitionTo(ProductStatus.DRAFT)) {
            throw new IllegalStateException("Cannot unpublish product in " + this.status + " status");
        }
        this.status = ProductStatus.DRAFT;
        this.publishedAt = null;
    }

    public void updateBasicInfo(String name, String description, String shortDescription, BigDecimal basePrice) {
        if (name != null) this.name = name;
        if (description != null) this.description = description;
        if (shortDescription != null) this.shortDescription = shortDescription;
        if (basePrice != null) this.basePrice = basePrice;
    }

    public void updatePricing(BigDecimal basePrice, BigDecimal compareAtPrice, BigDecimal costPrice) {
        if (basePrice != null) this.basePrice = basePrice;
        if (compareAtPrice != null) this.compareAtPrice = compareAtPrice;
        if (costPrice != null) this.costPrice = costPrice;
    }

    public void updateSEO(String metaTitle, String metaDescription, String metaKeywords) {
        if (metaTitle != null) this.metaTitle = metaTitle;
        if (metaDescription != null) this.metaDescription = metaDescription;
        if (metaKeywords != null) this.metaKeywords = metaKeywords;
    }

    public void updateShipping(BigDecimal weight, String dimensions, Boolean requiresShipping) {
        if (weight != null) this.weight = weight;
        if (dimensions != null) this.dimensions = dimensions;
        if (requiresShipping != null) this.requiresShipping = requiresShipping;
    }

    public void addVariant(ProductVariant variant) {
        variants.add(variant);
        variant.setProduct(this);
    }

    public void removeVariant(ProductVariant variant) {
        variants.remove(variant);
        variant.setProduct(null);
    }

    public void addImage(ProductImage image) {
        images.add(image);
        image.setProduct(this);
    }

    public void removeImage(ProductImage image) {
        images.remove(image);
        image.setProduct(null);
    }

    public void incrementViewCount() {
        this.viewCount++;
    }

    public void incrementTotalSold(int quantity) {
        this.totalSold += quantity;
    }

    public void updateRating(BigDecimal newRating, int reviewCount) {
        this.averageRating = newRating;
        this.reviewCount = reviewCount;
    }

    public void setFeatured(boolean featured) {
        this.isFeatured = featured;
    }

    public void setDigital(boolean digital) {
        this.isDigital = digital;
    }

    public boolean isOwner(Long vendorId) {
        return this.vendorId.equals(vendorId);
    }

    public boolean isPublished() {
        return this.status == ProductStatus.APPROVED;
    }

    public boolean canBeEdited() {
        return this.status.canBeEdited();
    }

    public boolean hasVariants() {
        return !variants.isEmpty();
    }

    public BigDecimal getLowestPrice() {
        if (variants.isEmpty()) {
            return basePrice;
        }
        return variants.stream()
            .filter(ProductVariant::getIsActive)
            .map(ProductVariant::getPrice)
            .min(BigDecimal::compareTo)
            .orElse(basePrice);
    }

    public BigDecimal getHighestPrice() {
        if (variants.isEmpty()) {
            return basePrice;
        }
        return variants.stream()
            .filter(ProductVariant::getIsActive)
            .map(ProductVariant::getPrice)
            .max(BigDecimal::compareTo)
            .orElse(basePrice);
    }

    public int getTotalInventory() {
        if (variants.isEmpty()) {
            return 0;
        }
        return variants.stream()
            .mapToInt(ProductVariant::getInventoryQuantity)
            .sum();
    }

    public boolean isInStock() {
        if (variants.isEmpty()) {
            return true;
        }
        return variants.stream()
            .filter(ProductVariant::getIsActive)
            .anyMatch(ProductVariant::isInStock);
    }
}
