package com.marketplace.order.infrastructure.persistence.mapper;

import com.marketplace.order.domain.entity.OrderItem;
import com.marketplace.order.infrastructure.persistence.entity.OrderItemJpaEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class OrderItemPersistenceMapper {

    private OrderItemPersistenceMapper() {}

    public static OrderItemJpaEntity toJpaEntity(OrderItem domain) {
        if (domain == null) return null;

        OrderItemJpaEntity jpa = new OrderItemJpaEntity();
        jpa.setId(domain.getId());
        jpa.setProductId(domain.getProductId());
        jpa.setVariantId(domain.getVariantId());
        jpa.setVendorId(domain.getVendorId());
        jpa.setProductName(domain.getProductName());
        jpa.setVariantName(domain.getVariantName());
        jpa.setUnitPrice(domain.getUnitPrice().getAmount());
        jpa.setQuantity(domain.getQuantity());
        jpa.setSubtotal(domain.getSubtotal().getAmount());
        jpa.setTaxAmount(domain.getTaxAmount().getAmount());
        jpa.setCommissionRate(domain.getCommissionRate().getAmount());
        jpa.setCommissionAmount(domain.getCommissionAmount().getAmount());
        jpa.setVendorPayout(domain.getVendorPayout().getAmount());
        jpa.setStatus(domain.getStatus());
        jpa.setSku(domain.getSku());
        jpa.setImageUrl(domain.getImageUrl());
        jpa.setNotes(domain.getNotes());
        jpa.setCreatedAt(domain.getCreatedAt());
        jpa.setUpdatedAt(domain.getUpdatedAt());

        return jpa;
    }

    public static OrderItem toDomain(OrderItemJpaEntity jpa) {
        if (jpa == null) return null;

        OrderItem item = new OrderItem(
            jpa.getProductId(),
            jpa.getVariantId(),
            jpa.getVendorId(),
            jpa.getProductName(),
            jpa.getVariantName(),
            jpa.getUnitPrice(),
            jpa.getQuantity(),
            jpa.getCommissionRate(),
            "USD"
        );

        setId(item, jpa.getId());
        item.setStatus(jpa.getStatus());
        item.setSku(jpa.getSku());
        item.setImageUrl(jpa.getImageUrl());
        item.setNotes(jpa.getNotes());
        item.setCreatedAt(jpa.getCreatedAt());
        item.setUpdatedAt(jpa.getUpdatedAt());

        return item;
    }

    public static List<OrderItem> toDomainList(List<OrderItemJpaEntity> jpaList) {
        if (jpaList == null) return Collections.emptyList();
        List<OrderItem> domains = new ArrayList<>();
        for (OrderItemJpaEntity jpa : jpaList) {
            domains.add(toDomain(jpa));
        }
        return domains;
    }

    private static void setId(OrderItem item, Long id) {
        try {
            Field field = OrderItem.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(item, id);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot set order item ID", e);
        }
    }
}