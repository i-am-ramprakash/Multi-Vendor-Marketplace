package com.marketplace.cart.infrastructure.persistence.mapper;

import com.marketplace.cart.domain.entity.Cart;
import com.marketplace.cart.domain.entity.CartItem;
import com.marketplace.cart.infrastructure.persistence.entity.CartItemJpaEntity;
import com.marketplace.cart.infrastructure.persistence.entity.CartJpaEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class CartPersistenceMapper {

    private CartPersistenceMapper() {}

    public static CartJpaEntity toJpaEntity(Cart domain) {
        if (domain == null) return null;

        CartJpaEntity jpa = new CartJpaEntity();
        jpa.setId(domain.getId());
        jpa.setUserId(domain.getUserId());
        jpa.setSessionId(domain.getSessionId());
        jpa.setStatus(domain.getStatus());
        jpa.setCurrency(domain.getCurrency());
        jpa.setSubtotal(domain.getSubtotal());
        jpa.setTaxAmount(domain.getTaxAmount());
        jpa.setDiscountAmount(domain.getDiscountAmount());
        jpa.setTotal(domain.getTotal());
        jpa.setCouponCode(domain.getCouponCode());
        jpa.setCreatedAt(domain.getCreatedAt());
        jpa.setUpdatedAt(domain.getUpdatedAt());
        jpa.setExpiresAt(domain.getExpiresAt());

        return jpa;
    }

    public static Cart toDomain(CartJpaEntity jpa) {
        if (jpa == null) return null;

        Cart cart;
        if (jpa.getUserId() != null) {
            cart = new Cart(jpa.getUserId());
        } else {
            cart = new Cart(jpa.getSessionId());
        }

        setId(cart, jpa.getId());
        cart.setStatus(jpa.getStatus());
        cart.setCurrency(jpa.getCurrency());
        cart.setSubtotal(jpa.getSubtotal());
        cart.setTaxAmount(jpa.getTaxAmount());
        cart.setDiscountAmount(jpa.getDiscountAmount());
        cart.setTotal(jpa.getTotal());
        cart.setCouponCode(jpa.getCouponCode());
        cart.setCreatedAt(jpa.getCreatedAt());
        cart.setUpdatedAt(jpa.getUpdatedAt());
        cart.setExpiresAt(jpa.getExpiresAt());

        if (jpa.getItems() != null) {
            for (CartItemJpaEntity itemJpa : jpa.getItems()) {
                CartItem item = CartItemPersistenceMapper.toDomain(itemJpa);
                cart.getItems().add(item);
            }
        }

        return cart;
    }

    private static void setId(Cart cart, Long id) {
        try {
            Field field = Cart.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(cart, id);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot set cart ID", e);
        }
    }
}