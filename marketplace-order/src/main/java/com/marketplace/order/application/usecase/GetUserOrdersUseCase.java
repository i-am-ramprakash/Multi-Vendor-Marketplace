package com.marketplace.order.application.usecase;

import com.marketplace.order.application.dto.OrderListResponse;
import com.marketplace.order.application.dto.OrderSummaryResponse;
import com.marketplace.order.domain.entity.Order;
import com.marketplace.order.domain.repository.OrderRepository;
import com.marketplace.order.domain.valueobject.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetUserOrdersUseCase {

    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public OrderListResponse execute(Long userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Order> orderPage = orderRepository.findByUserId(userId, pageRequest);

        List<OrderSummaryResponse> orders = orderPage.getContent().stream()
            .map(OrderSummaryResponse::from)
            .collect(Collectors.toList());

        return OrderListResponse.builder()
            .orders(orders)
            .page(page)
            .size(size)
            .totalElements(orderPage.getTotalElements())
            .totalPages(orderPage.getTotalPages())
            .first(page == 0)
            .last(page == orderPage.getTotalPages() - 1)
            .build();
    }

    @Transactional(readOnly = true)
    public OrderListResponse execute(Long userId, String status, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        OrderStatus orderStatus = OrderStatus.valueOf(status);
        Page<Order> orderPage = orderRepository.findByUserIdAndStatus(userId, orderStatus, pageRequest);

        List<OrderSummaryResponse> orders = orderPage.getContent().stream()
            .map(OrderSummaryResponse::from)
            .collect(Collectors.toList());

        return OrderListResponse.builder()
            .orders(orders)
            .page(page)
            .size(size)
            .totalElements(orderPage.getTotalElements())
            .totalPages(orderPage.getTotalPages())
            .first(page == 0)
            .last(page == orderPage.getTotalPages() - 1)
            .build();
    }
}