package com.marketplace.order.infrastructure.persistence.mapper;

import com.marketplace.order.domain.entity.OrderStatusHistory;
import com.marketplace.order.infrastructure.persistence.entity.OrderStatusHistoryJpaEntity;

import java.lang.reflect.Field;

public final class OrderStatusHistoryPersistenceMapper {

    private OrderStatusHistoryPersistenceMapper() {}

    public static OrderStatusHistoryJpaEntity toJpaEntity(OrderStatusHistory domain) {
        if (domain == null) return null;

        OrderStatusHistoryJpaEntity jpa = new OrderStatusHistoryJpaEntity();
        jpa.setId(domain.getId());
        jpa.setStatus(domain.getStatus());
        jpa.setNotes(domain.getNotes());
        jpa.setCreatedAt(domain.getCreatedAt());

        return jpa;
    }

    public static OrderStatusHistory toDomain(OrderStatusHistoryJpaEntity jpa) {
        if (jpa == null) return null;

        OrderStatusHistory history = new OrderStatusHistory(jpa.getStatus(), jpa.getNotes());
        setId(history, jpa.getId());
        history.setCreatedAt(jpa.getCreatedAt());

        return history;
    }

    private static void setId(OrderStatusHistory history, Long id) {
        try {
            Field field = OrderStatusHistory.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(history, id);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot set order status history ID", e);
        }
    }
}