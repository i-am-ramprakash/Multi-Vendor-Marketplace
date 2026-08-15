package com.marketplace.order.application.usecase;

import com.marketplace.order.application.dto.OrderResponse;
import com.marketplace.order.domain.entity.Order;
import com.marketplace.order.domain.event.OrderCancelledEvent;
import com.marketplace.order.domain.exception.OrderNotFoundException;
import com.marketplace.order.domain.repository.OrderRepository;
import com.marketplace.order.domain.service.InventoryService;
import com.marketplace.order.domain.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CancelOrderUseCase {

    private final OrderRepository orderRepository;
    private final InventoryService inventoryService;
    private final PaymentService paymentService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public OrderResponse execute(Long orderId, Long userId, String reason) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException(orderId));

        // Verify ownership
        if (!order.getUserId().equals(userId)) {
            throw new SecurityException("You are not authorized to cancel this order");
        }

        // Check if order can be cancelled
        if (!order.canBeCancelled()) {
            throw new IllegalStateException("Order cannot be cancelled in " + order.getStatus() + " status");
        }

        // Release inventory
        for (var item : order.getItems()) {
            inventoryService.releaseInventory(
                item.getProductId(),
                item.getVariantId(),
                item.getQuantity(),
                orderId
            );
        }

        // Process refund if payment was made
        if (order.getPaymentReference() != null) {
            paymentService.refundPayment(order.getPaymentReference(), order.getTotal(), reason);
        }

        // Cancel order
        order.cancel(reason);

        // Add audit log
        order.addAuditLog("ORDER_CANCELLED", userId, "Order cancelled. Reason: " + reason);

        // Save order
        Order savedOrder = orderRepository.save(order);

        // Publish event
        eventPublisher.publishEvent(new OrderCancelledEvent(
            this,
            savedOrder.getId(),
            savedOrder.getOrderNumber().getValue(),
            savedOrder.getUserId(),
            reason
        ));

        return OrderResponse.from(savedOrder);
    }
}