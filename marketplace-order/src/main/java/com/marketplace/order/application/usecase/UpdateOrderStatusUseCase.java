package com.marketplace.order.application.usecase;

import com.marketplace.order.application.dto.OrderResponse;
import com.marketplace.order.domain.entity.Order;
import com.marketplace.order.domain.event.OrderStatusChangedEvent;
import com.marketplace.order.domain.exception.OrderNotFoundException;
import com.marketplace.order.domain.repository.OrderRepository;
import com.marketplace.order.domain.service.InventoryService;
import com.marketplace.order.domain.valueobject.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UpdateOrderStatusUseCase {

    private final OrderRepository orderRepository;
    private final InventoryService inventoryService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public OrderResponse execute(Long orderId, String newStatus, Long performedBy) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException(orderId));

        OrderStatus oldStatus = order.getStatus();
        OrderStatus status = OrderStatus.valueOf(newStatus);

        // Update status
        switch (status) {
            case CONFIRMED -> order.confirm();
            case PROCESSING -> order.startProcessing();
            case SHIPPED -> order.ship("Tracking pending");
            case DELIVERED -> {
                order.deliver();
                // Deduct inventory on delivery
                for (var item : order.getItems()) {
                    inventoryService.deductInventory(
                        item.getProductId(),
                        item.getVariantId(),
                        item.getQuantity(),
                        orderId
                    );
                }
            }
            default -> throw new IllegalStateException("Invalid status transition to " + status);
        }

        // Add audit log
        order.addAuditLog("STATUS_CHANGED", performedBy,
            "Status changed from " + oldStatus + " to " + status);

        // Save order
        Order savedOrder = orderRepository.save(order);

        // Publish event
        eventPublisher.publishEvent(new OrderStatusChangedEvent(
            this,
            savedOrder.getId(),
            savedOrder.getOrderNumber().getValue(),
            oldStatus,
            status,
            performedBy
        ));

        return OrderResponse.from(savedOrder);
    }
}