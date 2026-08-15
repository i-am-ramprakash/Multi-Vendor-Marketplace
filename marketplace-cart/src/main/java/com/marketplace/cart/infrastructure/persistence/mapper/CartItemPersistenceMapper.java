package com.marketplace.cart.infrastructure.persistence.mapper;

import com.marketplace.cart.domain.entity.CartItem;
import com.marketplace.cart.infrastructure.persistence.entity.CartItemJpaEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class CartItemPersistenceMapper {

    private CartItemPersistenceMapper() {}

    public static CartItemJpaEntity toJpaEntity(CartItem domain) {
        if (domain == null) return null;

        CartItemJpaEntity jpa = new CartItemJpaEntity();
        jpa.setId(domain.getId());
        jpa.setProductId(domain.getProductId());
        jpa.setVariantId(domain.getVariantId());
        jpa.setProductName(domain.getProductName());
        jpa.setVariantName(domain.getVariantName());
        jpa.setUnitPrice(domain.getUnitPrice());
        jpa.setQuantity(domain.getQuantity());
        jpa.setSubtotal(domain.getSubtotal());
        jpa.setImageUrl(domain.getImageUrl());
        jpa.setMaxQuantity(domain.getMaxQuantity());
        jpa.setInventoryAvailable(domain.isInventoryAvailable());
        jpa.setAddedAt(domain.getAddedAt());
        jpa.setUpdatedAt(domain.getUpdatedAt());

        return jpa;
    }

    public static CartItem toDomain(CartItemJpaEntity jpa) {
        if (jpa == null) return null;

        CartItem item = new CartItem(
            jpa.getProductId(),
            jpa.getVariantId(),
            jpa.getProductName(),
            jpa.getVariantName(),
            jpa.getUnitPrice(),
            jpa.getQuantity(),
            jpa.getImageUrl()
        );

        setId(item, jpa.getId());
        item.setMaxQuantity(jpa.getMaxQuantity());
        item.setInventoryAvailable(jpa.getInventoryAvailable());
        item.setAddedAt(jpa.getAddedAt());
        item.setUpdatedAt(jpa.getUpdatedAt());

        return item;
    }

    public static List<CartItem> toDomainList(List<CartItemJpaEntity> jpaList) {
        if (jpaList == null) return Collections.emptyList();
        List<CartItem> domains = new ArrayList<>();
        for (CartItemJpaEntity jpa : jpaList) {
            domains.add(toDomain(jpa));
        }
        return domains;
    }

    private static void setId(CartItem item, Long id) {
        try {
            Field field = CartItem.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(item, id);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot set cart item ID", e);
        }
    }
}