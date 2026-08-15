package com.marketplace.product.infrastructure.persistence.mapper;

import com.marketplace.product.domain.entity.ProductVariant;
import com.marketplace.product.domain.valueobject.SKU;
import com.marketplace.product.infrastructure.persistence.entity.ProductVariantJpaEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ProductVariantPersistenceMapper {

    private ProductVariantPersistenceMapper() {}

    public static ProductVariantJpaEntity toJpaEntity(ProductVariant domain) {
        if (domain == null) return null;

        ProductVariantJpaEntity jpa = new ProductVariantJpaEntity();
        jpa.setId(domain.getId());
        jpa.setName(domain.getName());
        jpa.setSku(domain.getSku() != null ? domain.getSku().getValue() : null);
        jpa.setBarcode(domain.getBarcode());
        jpa.setPrice(domain.getPrice());
        jpa.setCompareAtPrice(domain.getCompareAtPrice());
        jpa.setCostPrice(domain.getCostPrice());
        jpa.setWeight(domain.getWeight());
        jpa.setDimensions(domain.getDimensions());
        jpa.setInventoryQuantity(domain.getInventoryQuantity());
        jpa.setLowStockThreshold(domain.getLowStockThreshold());
        jpa.setTrackInventory(domain.getTrackInventory());
        jpa.setAllowBackorder(domain.getAllowBackorder());
        jpa.setImageUrl(domain.getImageUrl());
        jpa.setPosition(domain.getPosition());
        jpa.setIsActive(domain.getIsActive());
        jpa.setCreatedAt(domain.getCreatedAt());
        jpa.setUpdatedAt(domain.getUpdatedAt());

        if (domain.getProduct() != null) {
            com.marketplace.product.infrastructure.persistence.entity.ProductJpaEntity productJpa = new com.marketplace.product.infrastructure.persistence.entity.ProductJpaEntity();
            productJpa.setId(domain.getProduct().getId());
            jpa.setProduct(productJpa);
        }

        return jpa;
    }

    public static ProductVariant toDomain(ProductVariantJpaEntity jpa) {
        if (jpa == null) return null;

        ProductVariant variant = new ProductVariant(
            jpa.getName(),
            jpa.getPrice(),
            jpa.getSku() != null ? SKU.of(jpa.getSku()) : null
        );

        setId(variant, jpa.getId());
        variant.setBarcode(jpa.getBarcode());
        variant.setCompareAtPrice(jpa.getCompareAtPrice());
        variant.setCostPrice(jpa.getCostPrice());
        variant.setWeight(jpa.getWeight());
        variant.setDimensions(jpa.getDimensions());
        variant.setInventoryQuantity(jpa.getInventoryQuantity());
        variant.setLowStockThreshold(jpa.getLowStockThreshold());
        variant.setTrackInventory(jpa.getTrackInventory());
        variant.setAllowBackorder(jpa.getAllowBackorder());
        variant.setImageUrl(jpa.getImageUrl());
        variant.setPosition(jpa.getPosition());
        variant.setIsActive(jpa.getIsActive());
        variant.setCreatedAt(jpa.getCreatedAt());
        variant.setUpdatedAt(jpa.getUpdatedAt());

        return variant;
    }

    public static List<ProductVariant> toDomainList(List<ProductVariantJpaEntity> jpaList) {
        if (jpaList == null) return Collections.emptyList();
        List<ProductVariant> domains = new ArrayList<>();
        for (ProductVariantJpaEntity jpa : jpaList) {
            domains.add(toDomain(jpa));
        }
        return domains;
    }

    private static void setId(ProductVariant variant, Long id) {
        try {
            Field field = ProductVariant.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(variant, id);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot set variant ID", e);
        }
    }
}