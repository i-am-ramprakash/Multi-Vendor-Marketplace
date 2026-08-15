package com.marketplace.wishlist.infrastructure.persistence.mapper;

import com.marketplace.wishlist.domain.entity.WishlistItem;
import com.marketplace.wishlist.infrastructure.persistence.entity.WishlistItemJpaEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class WishlistItemPersistenceMapper {

    private WishlistItemPersistenceMapper() {}

    public static WishlistItemJpaEntity toJpaEntity(WishlistItem domain) {
        if (domain == null) return null;

        WishlistItemJpaEntity jpa = new WishlistItemJpaEntity();
        jpa.setId(domain.getId());
        jpa.setProductId(domain.getProductId());
        jpa.setVariantId(domain.getVariantId());
        jpa.setProductName(domain.getProductName());
        jpa.setVariantName(domain.getVariantName());
        jpa.setUnitPrice(domain.getUnitPrice());
        jpa.setImageUrl(domain.getImageUrl());
        jpa.setVendorName(domain.getVendorName());
        jpa.setVendorId(domain.getVendorId());
        jpa.setIsAvailable(domain.isAvailable());
        jpa.setAddedAt(domain.getAddedAt());
        jpa.setUpdatedAt(domain.getUpdatedAt());

        return jpa;
    }

    public static WishlistItem toDomain(WishlistItemJpaEntity jpa) {
        if (jpa == null) return null;

        WishlistItem item = new WishlistItem(
            jpa.getProductId(),
            jpa.getVariantId(),
            jpa.getProductName(),
            jpa.getVariantName(),
            jpa.getUnitPrice(),
            jpa.getImageUrl(),
            jpa.getVendorName(),
            jpa.getVendorId()
        );

        setId(item, jpa.getId());
        item.setAvailable(jpa.getIsAvailable());
        item.setAddedAt(jpa.getAddedAt());
        item.setUpdatedAt(jpa.getUpdatedAt());

        return item;
    }

    public static List<WishlistItem> toDomainList(List<WishlistItemJpaEntity> jpaList) {
        if (jpaList == null) return Collections.emptyList();
        List<WishlistItem> domains = new ArrayList<>();
        for (WishlistItemJpaEntity jpa : jpaList) {
            domains.add(toDomain(jpa));
        }
        return domains;
    }

    private static void setId(WishlistItem item, Long id) {
        try {
            Field field = WishlistItem.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(item, id);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot set wishlist item ID", e);
        }
    }
}