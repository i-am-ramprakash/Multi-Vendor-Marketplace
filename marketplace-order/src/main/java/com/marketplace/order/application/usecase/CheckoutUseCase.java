package com.marketplace.order.application.usecase;

import com.marketplace.order.application.dto.CheckoutRequest;
import com.marketplace.order.application.dto.OrderResponse;
import com.marketplace.order.domain.entity.Order;
import com.marketplace.order.domain.event.OrderCreatedEvent;
import com.marketplace.order.domain.exception.EmptyCartException;
import com.marketplace.order.domain.exception.InsufficientInventoryException;
import com.marketplace.order.domain.repository.OrderRepository;
import com.marketplace.order.domain.service.InventoryService;
import com.marketplace.order.domain.service.PaymentService;
import com.marketplace.order.domain.valueobject.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class CheckoutUseCase {

    private final OrderRepository orderRepository;
    private final InventoryService inventoryService;
    private final PaymentService paymentService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public OrderResponse execute(CheckoutRequest request) {
        // Validate cart is not empty
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new EmptyCartException();
        }

        // Create order
        Order order = new Order(request.getUserId(), "USD");

        // Add items and check inventory
        for (CheckoutRequest.CheckoutItemRequest itemRequest : request.getItems()) {
            // Check inventory availability
            if (!inventoryService.checkAvailability(itemRequest.getProductId(), itemRequest.getVariantId(), itemRequest.getQuantity())) {
                throw new InsufficientInventoryException(itemRequest.getProductName(), itemRequest.getQuantity(), 0);
            }

            // Add item to order
            order.addItem(
                itemRequest.getProductId(),
                itemRequest.getVariantId(),
                itemRequest.getVendorId(),
                itemRequest.getProductName(),
                itemRequest.getVariantName(),
                itemRequest.getUnitPrice(),
                itemRequest.getQuantity(),
                BigDecimal.TEN // Default 10% commission rate
            );
        }

        // Set shipping and billing addresses
        order.setShippingAddress(request.getShippingAddress());
        if (request.getBillingAddress() != null) {
            order.setBillingAddress(request.getBillingAddress());
        } else {
            order.setBillingAddress(request.getShippingAddress());
        }

        // Set notes
        if (request.getNotes() != null) {
            order.setNotes(request.getNotes());
        }

        // Process payment
        String paymentReference = paymentService.processPayment(
            request.getUserId(),
            order.getTotal(),
            request.getPaymentMethod(),
            request.getPaymentReference()
        );

        order.setPaymentInfo(request.getPaymentMethod(), paymentReference);

        // Reserve inventory
        for (var item : order.getItems()) {
            inventoryService.reserveInventory(
                item.getProductId(),
                item.getVariantId(),
                item.getQuantity(),
                null
            );
        }

        // Save order
        Order savedOrder = orderRepository.save(order);

        // Add audit log
        savedOrder.addAuditLog("ORDER_CREATED", request.getUserId(), "Order placed successfully");

        // Publish event
        eventPublisher.publishEvent(new OrderCreatedEvent(
            this,
            savedOrder.getId(),
            savedOrder.getOrderNumber().getValue(),
            savedOrder.getUserId(),
            savedOrder.getTotal().getAmount(),
            savedOrder.getCurrency()
        ));

        return OrderResponse.from(savedOrder);
    }
}