package com.marketplace.product.infrastructure.persistence.mapper;

import com.marketplace.product.domain.entity.ProductImage;
import com.marketplace.product.infrastructure.persistence.entity.ProductImageJpaEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ProductImagePersistenceMapper {

    private ProductImagePersistenceMapper() {}

    public static ProductImageJpaEntity toJpaEntity(ProductImage domain) {
        if (domain == null) return null;

        ProductImageJpaEntity jpa = new ProductImageJpaEntity();
        jpa.setId(domain.getId());
        jpa.setUrl(domain.getUrl());
        jpa.setAltText(domain.getAltText());
        jpa.setPosition(domain.getPosition());
        jpa.setIsPrimary(domain.getIsPrimary());
        jpa.setCreatedAt(domain.getCreatedAt());

        if (domain.getProduct() != null) {
            com.marketplace.product.infrastructure.persistence.entity.ProductJpaEntity productJpa = new com.marketplace.product.infrastructure.persistence.entity.ProductJpaEntity();
            productJpa.setId(domain.getProduct().getId());
            jpa.setProduct(productJpa);
        }

        if (domain.getVariant() != null) {
            com.marketplace.product.infrastructure.persistence.entity.ProductVariantJpaEntity variantJpa = new com.marketplace.product.infrastructure.persistence.entity.ProductVariantJpaEntity();
            variantJpa.setId(domain.getVariant().getId());
            jpa.setVariant(variantJpa);
        }

        return jpa;
    }

    public static ProductImage toDomain(ProductImageJpaEntity jpa) {
        if (jpa == null) return null;

        ProductImage image = new ProductImage(
            jpa.getUrl(),
            jpa.getAltText(),
            jpa.getPosition(),
            jpa.getIsPrimary()
        );

        setId(image, jpa.getId());
        image.setCreatedAt(jpa.getCreatedAt());

        return image;
    }

    public static List<ProductImage> toDomainList(List<ProductImageJpaEntity> jpaList) {
        if (jpaList == null) return Collections.emptyList();
        List<ProductImage> domains = new ArrayList<>();
        for (ProductImageJpaEntity jpa : jpaList) {
            domains.add(toDomain(jpa));
        }
        return domains;
    }

    private static void setId(ProductImage image, Long id) {
        try {
            Field field = ProductImage.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(image, id);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot set image ID", e);
        }
    }
}