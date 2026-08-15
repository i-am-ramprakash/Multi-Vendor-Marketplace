package com.marketplace.order.infrastructure.persistence.mapper;

import com.marketplace.order.domain.entity.Order;
import com.marketplace.order.domain.entity.OrderAuditLog;
import com.marketplace.order.domain.entity.OrderItem;
import com.marketplace.order.domain.entity.OrderStatusHistory;
import com.marketplace.order.domain.valueobject.Money;
import com.marketplace.order.domain.valueobject.OrderNumber;
import com.marketplace.order.infrastructure.persistence.entity.OrderAuditLogJpaEntity;
import com.marketplace.order.infrastructure.persistence.entity.OrderItemJpaEntity;
import com.marketplace.order.infrastructure.persistence.entity.OrderJpaEntity;
import com.marketplace.order.infrastructure.persistence.entity.OrderStatusHistoryJpaEntity;

import java.lang.reflect.Field;

public final class OrderPersistenceMapper {

    private OrderPersistenceMapper() {}

    public static OrderJpaEntity toJpaEntity(Order domain) {
        if (domain == null) return null;

        OrderJpaEntity jpa = new OrderJpaEntity();
        jpa.setId(domain.getId());
        jpa.setOrderNumber(domain.getOrderNumber().getValue());
        jpa.setUserId(domain.getUserId());
        jpa.setStatus(domain.getStatus());
        jpa.setSubtotal(domain.getSubtotal().getAmount());
        jpa.setTaxAmount(domain.getTaxAmount().getAmount());
        jpa.setShippingAmount(domain.getShippingAmount().getAmount());
        jpa.setDiscountAmount(domain.getDiscountAmount().getAmount());
        jpa.setCommissionAmount(domain.getCommissionAmount().getAmount());
        jpa.setTotal(domain.getTotal().getAmount());
        jpa.setCurrency(domain.getCurrency());
        jpa.setPaymentMethod(domain.getPaymentMethod());
        jpa.setPaymentReference(domain.getPaymentReference());
        jpa.setShippingAddress(domain.getShippingAddress());
        jpa.setBillingAddress(domain.getBillingAddress());
        jpa.setNotes(domain.getNotes());
        jpa.setCancellationReason(domain.getCancellationReason());
        jpa.setShippedAt(domain.getShippedAt());
        jpa.setDeliveredAt(domain.getDeliveredAt());
        jpa.setCancelledAt(domain.getCancelledAt());
        jpa.setCreatedAt(domain.getCreatedAt());
        jpa.setUpdatedAt(domain.getUpdatedAt());
        jpa.setVersion(domain.getVersion());

        return jpa;
    }

    public static Order toDomain(OrderJpaEntity jpa) {
        if (jpa == null) return null;

        Order order = new Order(jpa.getUserId(), jpa.getCurrency());
        setId(order, jpa.getId());
        order.setOrderNumber(OrderNumber.of(jpa.getOrderNumber()));
        order.setStatus(jpa.getStatus());
        order.setSubtotal(Money.of(jpa.getSubtotal(), jpa.getCurrency()));
        order.setTaxAmount(Money.of(jpa.getTaxAmount(), jpa.getCurrency()));
        order.setShippingAmount(Money.of(jpa.getShippingAmount(), jpa.getCurrency()));
        order.setDiscountAmount(Money.of(jpa.getDiscountAmount(), jpa.getCurrency()));
        order.setCommissionAmount(Money.of(jpa.getCommissionAmount(), jpa.getCurrency()));
        order.setTotal(Money.of(jpa.getTotal(), jpa.getCurrency()));
        order.setPaymentMethod(jpa.getPaymentMethod());
        order.setPaymentReference(jpa.getPaymentReference());
        order.setShippingAddress(jpa.getShippingAddress());
        order.setBillingAddress(jpa.getBillingAddress());
        order.setNotes(jpa.getNotes());
        order.setCancellationReason(jpa.getCancellationReason());
        order.setShippedAt(jpa.getShippedAt());
        order.setDeliveredAt(jpa.getDeliveredAt());
        order.setCancelledAt(jpa.getCancelledAt());
        order.setCreatedAt(jpa.getCreatedAt());
        order.setUpdatedAt(jpa.getUpdatedAt());
        order.setVersion(jpa.getVersion());

        if (jpa.getItems() != null) {
            for (OrderItemJpaEntity itemJpa : jpa.getItems()) {
                OrderItem item = OrderItemPersistenceMapper.toDomain(itemJpa);
                order.getItems().add(item);
            }
        }

        if (jpa.getStatusHistory() != null) {
            for (OrderStatusHistoryJpaEntity historyJpa : jpa.getStatusHistory()) {
                OrderStatusHistory history = OrderStatusHistoryPersistenceMapper.toDomain(historyJpa);
                order.getStatusHistory().add(history);
            }
        }

        if (jpa.getAuditLogs() != null) {
            for (OrderAuditLogJpaEntity auditJpa : jpa.getAuditLogs()) {
                OrderAuditLog auditLog = OrderAuditLogPersistenceMapper.toDomain(auditJpa);
                order.getAuditLogs().add(auditLog);
            }
        }

        return order;
    }

    private static void setId(Order order, Long id) {
        try {
            Field field = Order.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(order, id);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot set order ID", e);
        }
    }
}