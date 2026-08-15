package com.marketplace.order.application.service;

import com.marketplace.order.application.dto.*;
import com.marketplace.order.domain.valueobject.OrderStatus;

public interface OrderService {

    OrderResponse checkout(CheckoutRequest request);

    OrderResponse getOrder(Long orderId);

    OrderResponse getOrderByOrderNumber(String orderNumber);

    OrderListResponse getUserOrders(Long userId, int page, int size);

    OrderListResponse getUserOrdersByStatus(Long userId, OrderStatus status, int page, int size);

    OrderResponse cancelOrder(Long orderId, Long userId, String reason);

    OrderResponse updateOrderStatus(Long orderId, String status, Long performedBy);

    OrderListResponse getVendorOrders(Long vendorId, int page, int size);

    OrderListResponse getVendorOrdersByStatus(Long vendorId, OrderStatus status, int page, int size);
}