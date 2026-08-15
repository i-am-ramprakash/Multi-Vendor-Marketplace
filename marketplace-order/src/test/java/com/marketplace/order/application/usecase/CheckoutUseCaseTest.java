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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckoutUseCaseTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private InventoryService inventoryService;

    @Mock
    private PaymentService paymentService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private CheckoutUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CheckoutUseCase(orderRepository, inventoryService, paymentService, eventPublisher);
    }

    @Test
    void execute_WithValidRequest_ShouldCreateOrder() {
        // Given
        CheckoutRequest request = CheckoutRequest.builder()
            .userId(1L)
            .paymentMethod("CREDIT_CARD")
            .paymentReference("PAY-123")
            .shippingAddress("123 Main St, New York, NY 10001")
            .items(List.of(
                CheckoutRequest.CheckoutItemRequest.builder()
                    .productId(100L)
                    .variantId(200L)
                    .vendorId(10L)
                    .productName("Wireless Headphones")
                    .variantName("Black")
                    .unitPrice(new BigDecimal("99.99"))
                    .quantity(2)
                    .build()
            ))
            .build();

        when(inventoryService.checkAvailability(100L, 200L, 2)).thenReturn(true);
        when(paymentService.processPayment(eq(1L), any(Money.class), eq("CREDIT_CARD"), eq("PAY-123")))
            .thenReturn("PAY-123");
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            setField(order, "id", 1L);
            return order;
        });

        // When
        OrderResponse response = useCase.execute(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isEqualTo(1L);
        assertThat(response.getStatus()).isEqualTo("PENDING");
        assertThat(response.getPaymentMethod()).isEqualTo("CREDIT_CARD");
        assertThat(response.getItemCount()).isEqualTo(1);

        verify(inventoryService).checkAvailability(100L, 200L, 2);
        verify(inventoryService).reserveInventory(100L, 200L, 2, null);
        verify(orderRepository).save(any(Order.class));
        verify(eventPublisher).publishEvent(any(OrderCreatedEvent.class));
    }

    @Test
    void execute_WithEmptyCart_ShouldThrowEmptyCartException() {
        // Given
        CheckoutRequest request = CheckoutRequest.builder()
            .userId(1L)
            .paymentMethod("CREDIT_CARD")
            .shippingAddress("123 Main St")
            .items(Collections.emptyList())
            .build();

        // When & Then
        assertThatThrownBy(() -> useCase.execute(request))
            .isInstanceOf(EmptyCartException.class);

        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void execute_WithNullItems_ShouldThrowEmptyCartException() {
        // Given
        CheckoutRequest request = CheckoutRequest.builder()
            .userId(1L)
            .paymentMethod("CREDIT_CARD")
            .shippingAddress("123 Main St")
            .build();

        // When & Then
        assertThatThrownBy(() -> useCase.execute(request))
            .isInstanceOf(EmptyCartException.class);

        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void execute_WithInsufficientInventory_ShouldThrowInsufficientInventoryException() {
        // Given
        CheckoutRequest request = CheckoutRequest.builder()
            .userId(1L)
            .paymentMethod("CREDIT_CARD")
            .shippingAddress("123 Main St")
            .items(List.of(
                CheckoutRequest.CheckoutItemRequest.builder()
                    .productId(100L)
                    .variantId(200L)
                    .vendorId(10L)
                    .productName("Wireless Headphones")
                    .unitPrice(new BigDecimal("99.99"))
                    .quantity(5)
                    .build()
            ))
            .build();

        when(inventoryService.checkAvailability(100L, 200L, 5)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> useCase.execute(request))
            .isInstanceOf(InsufficientInventoryException.class)
            .hasMessageContaining("Wireless Headphones");

        verify(orderRepository, never()).save(any(Order.class));
        verify(paymentService, never()).processPayment(any(), any(), any(), any());
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException("Cannot set field: " + fieldName, e);
        }
    }
}