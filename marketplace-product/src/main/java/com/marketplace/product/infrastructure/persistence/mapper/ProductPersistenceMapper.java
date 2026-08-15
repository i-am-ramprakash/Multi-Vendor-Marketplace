package com.marketplace.product.infrastructure.persistence.mapper;

import com.marketplace.product.domain.entity.Product;
import com.marketplace.product.domain.valueobject.ProductSlug;
import com.marketplace.product.domain.valueobject.SKU;
import com.marketplace.product.infrastructure.persistence.entity.ProductJpaEntity;

import java.lang.reflect.Field;

public final class ProductPersistenceMapper {

    private ProductPersistenceMapper() {}

    public static ProductJpaEntity toJpaEntity(Product domain) {
        if (domain == null) return null;

        ProductJpaEntity jpa = new ProductJpaEntity();
        jpa.setId(domain.getId());
        jpa.setVendorId(domain.getVendorId());
        jpa.setName(domain.getName());
        jpa.setSlug(domain.getSlug() != null ? domain.getSlug().getValue() : null);
        jpa.setDescription(domain.getDescription());
        jpa.setShortDescription(domain.getShortDescription());
        jpa.setBasePrice(domain.getBasePrice());
        jpa.setCompareAtPrice(domain.getCompareAtPrice());
        jpa.setCostPrice(domain.getCostPrice());
        jpa.setSku(domain.getSku() != null ? domain.getSku().getValue() : null);
        jpa.setBarcode(domain.getBarcode());
        jpa.setWeight(domain.getWeight());
        jpa.setDimensions(domain.getDimensions());
        jpa.setStatus(domain.getStatus());
        jpa.setIsFeatured(domain.getIsFeatured());
        jpa.setIsDigital(domain.getIsDigital());
        jpa.setRequiresShipping(domain.getRequiresShipping());
        jpa.setTaxClass(domain.getTaxClass());
        jpa.setMetaTitle(domain.getMetaTitle());
        jpa.setMetaDescription(domain.getMetaDescription());
        jpa.setMetaKeywords(domain.getMetaKeywords());
        jpa.setApprovedAt(domain.getApprovedAt());
        jpa.setApprovedBy(domain.getApprovedBy());
        jpa.setRejectionReason(domain.getRejectionReason());
        jpa.setPublishedAt(domain.getPublishedAt());
        jpa.setTotalSold(domain.getTotalSold());
        jpa.setViewCount(domain.getViewCount());
        jpa.setAverageRating(domain.getAverageRating());
        jpa.setReviewCount(domain.getReviewCount());
        jpa.setCreatedAt(domain.getCreatedAt());
        jpa.setUpdatedAt(domain.getUpdatedAt());
        jpa.setVersion(domain.getVersion());

        if (domain.getCategory() != null) {
            com.marketplace.product.infrastructure.persistence.entity.CategoryJpaEntity categoryJpa = new com.marketplace.product.infrastructure.persistence.entity.CategoryJpaEntity();
            categoryJpa.setId(domain.getCategory().getId());
            jpa.setCategory(categoryJpa);
        }

        return jpa;
    }

    public static Product toDomain(ProductJpaEntity jpa) {
        if (jpa == null) return null;

        Product product = new Product(
            jpa.getVendorId(),
            null,
            jpa.getName(),
            jpa.getSlug() != null ? ProductSlug.of(jpa.getSlug()) : null,
            jpa.getBasePrice()
        );

        setId(product, jpa.getId());
        product.setDescription(jpa.getDescription());
        product.setShortDescription(jpa.getShortDescription());
        product.setCompareAtPrice(jpa.getCompareAtPrice());
        product.setCostPrice(jpa.getCostPrice());
        product.setSku(jpa.getSku() != null ? SKU.of(jpa.getSku()) : null);
        product.setBarcode(jpa.getBarcode());
        product.setWeight(jpa.getWeight());
        product.setDimensions(jpa.getDimensions());
        product.setStatus(jpa.getStatus());
        product.setFeatured(jpa.getIsFeatured());
        product.setDigital(jpa.getIsDigital());
        product.setRequiresShipping(jpa.getRequiresShipping());
        product.setTaxClass(jpa.getTaxClass());
        product.setMetaTitle(jpa.getMetaTitle());
        product.setMetaDescription(jpa.getMetaDescription());
        product.setMetaKeywords(jpa.getMetaKeywords());
        product.setApprovedAt(jpa.getApprovedAt());
        product.setApprovedBy(jpa.getApprovedBy());
        product.setRejectionReason(jpa.getRejectionReason());
        product.setPublishedAt(jpa.getPublishedAt());
        product.setTotalSold(jpa.getTotalSold());
        product.setViewCount(jpa.getViewCount());
        product.setAverageRating(jpa.getAverageRating());
        product.setReviewCount(jpa.getReviewCount());
        product.setCreatedAt(jpa.getCreatedAt());
        product.setUpdatedAt(jpa.getUpdatedAt());
        product.setVersion(jpa.getVersion());

        return product;
    }

    private static void setId(Product product, Long id) {
        try {
            Field field = Product.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(product, id);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot set product ID", e);
        }
    }
}