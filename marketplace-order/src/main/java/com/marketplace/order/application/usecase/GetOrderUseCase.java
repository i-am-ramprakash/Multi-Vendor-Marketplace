package com.marketplace.order.application.usecase;

import com.marketplace.order.application.dto.OrderResponse;
import com.marketplace.order.domain.entity.Order;
import com.marketplace.order.domain.exception.OrderNotFoundException;
import com.marketplace.order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GetOrderUseCase {

    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public OrderResponse execute(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException(orderId));

        return OrderResponse.from(order);
    }

    @Transactional(readOnly = true)
    public OrderResponse executeByOrderNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
            .orElseThrow(() -> new OrderNotFoundException("Order not found with number: " + orderNumber));

        return OrderResponse.from(order);
    }
}