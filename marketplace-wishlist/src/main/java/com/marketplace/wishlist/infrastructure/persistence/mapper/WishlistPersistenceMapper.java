package com.marketplace.wishlist.infrastructure.persistence.mapper;

import com.marketplace.wishlist.domain.entity.Wishlist;
import com.marketplace.wishlist.domain.entity.WishlistItem;
import com.marketplace.wishlist.infrastructure.persistence.entity.WishlistItemJpaEntity;
import com.marketplace.wishlist.infrastructure.persistence.entity.WishlistJpaEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class WishlistPersistenceMapper {

    private WishlistPersistenceMapper() {}

    public static WishlistJpaEntity toJpaEntity(Wishlist domain) {
        if (domain == null) return null;

        WishlistJpaEntity jpa = new WishlistJpaEntity();
        jpa.setId(domain.getId());
        jpa.setUserId(domain.getUserId());
        jpa.setName(domain.getName());
        jpa.setIsDefault(domain.isDefault());
        jpa.setCreatedAt(domain.getCreatedAt());
        jpa.setUpdatedAt(domain.getUpdatedAt());

        return jpa;
    }

    public static Wishlist toDomain(WishlistJpaEntity jpa) {
        if (jpa == null) return null;

        Wishlist wishlist;
        if (jpa.getIsDefault()) {
            wishlist = new Wishlist(jpa.getUserId());
        } else {
            wishlist = new Wishlist(jpa.getUserId(), jpa.getName());
        }

        setId(wishlist, jpa.getId());
        wishlist.setCreatedAt(jpa.getCreatedAt());
        wishlist.setUpdatedAt(jpa.getUpdatedAt());

        if (jpa.getItems() != null) {
            for (WishlistItemJpaEntity itemJpa : jpa.getItems()) {
                WishlistItem item = WishlistItemPersistenceMapper.toDomain(itemJpa);
                wishlist.getItems().add(item);
            }
        }

        return wishlist;
    }

    private static void setId(Wishlist wishlist, Long id) {
        try {
            Field field = Wishlist.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(wishlist, id);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot set wishlist ID", e);
        }
    }
}