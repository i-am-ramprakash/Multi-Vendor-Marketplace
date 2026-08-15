package com.marketplace.order.application.usecase;

import com.marketplace.order.application.dto.OrderListResponse;
import com.marketplace.order.application.dto.OrderSummaryResponse;
import com.marketplace.order.domain.entity.Order;
import com.marketplace.order.domain.repository.OrderRepository;
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
public class GetVendorOrdersUseCase {

    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public OrderListResponse execute(Long vendorId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        // Search orders that contain items from this vendor
        Page<Order> orderPage = orderRepository.search(null, null, vendorId, null, pageRequest);

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
    public OrderListResponse execute(Long vendorId, String status, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        // Search orders with specific status that contain items from this vendor
        Page<Order> orderPage = orderRepository.search(null, null, vendorId,
            com.marketplace.order.domain.valueobject.OrderStatus.valueOf(status), pageRequest);

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