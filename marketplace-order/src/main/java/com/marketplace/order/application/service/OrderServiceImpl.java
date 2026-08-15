package com.marketplace.order.application.service;

import com.marketplace.order.application.dto.*;
import com.marketplace.order.application.usecase.*;
import com.marketplace.order.domain.valueobject.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final CheckoutUseCase checkoutUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final GetUserOrdersUseCase getUserOrdersUseCase;
    private final CancelOrderUseCase cancelOrderUseCase;
    private final UpdateOrderStatusUseCase updateOrderStatusUseCase;
    private final GetVendorOrdersUseCase getVendorOrdersUseCase;

    @Override
    @Transactional
    public OrderResponse checkout(CheckoutRequest request) {
        return checkoutUseCase.execute(request);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long orderId) {
        return getOrderUseCase.execute(orderId);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderByOrderNumber(String orderNumber) {
        return getOrderUseCase.executeByOrderNumber(orderNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderListResponse getUserOrders(Long userId, int page, int size) {
        return getUserOrdersUseCase.execute(userId, page, size);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderListResponse getUserOrdersByStatus(Long userId, OrderStatus status, int page, int size) {
        return getUserOrdersUseCase.execute(userId, status.name(), page, size);
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(Long orderId, Long userId, String reason) {
        return cancelOrderUseCase.execute(orderId, userId, reason);
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, String status, Long performedBy) {
        return updateOrderStatusUseCase.execute(orderId, status, performedBy);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderListResponse getVendorOrders(Long vendorId, int page, int size) {
        return getVendorOrdersUseCase.execute(vendorId, page, size);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderListResponse getVendorOrdersByStatus(Long vendorId, OrderStatus status, int page, int size) {
        return getVendorOrdersUseCase.execute(vendorId, status.name(), page, size);
    }
}