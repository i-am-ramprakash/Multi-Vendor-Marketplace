package com.marketplace.order.application.service;

import com.marketplace.order.application.dto.*;
import com.marketplace.order.application.usecase.*;
import com.marketplace.order.domain.entity.Order;
import com.marketplace.order.domain.valueobject.OrderNumber;
import com.marketplace.order.domain.valueobject.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private CheckoutUseCase checkoutUseCase;

    @Mock
    private GetOrderUseCase getOrderUseCase;

    @Mock
    private GetUserOrdersUseCase getUserOrdersUseCase;

    @Mock
    private CancelOrderUseCase cancelOrderUseCase;

    @Mock
    private UpdateOrderStatusUseCase updateOrderStatusUseCase;

    @Mock
    private GetVendorOrdersUseCase getVendorOrdersUseCase;

    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderServiceImpl(
            checkoutUseCase,
            getOrderUseCase,
            getUserOrdersUseCase,
            cancelOrderUseCase,
            updateOrderStatusUseCase,
            getVendorOrdersUseCase
        );
    }

    @Test
    void checkout_WithValidRequest_ShouldDelegateToUseCase() {
        // Given
        CheckoutRequest request = CheckoutRequest.builder()
            .userId(1L)
            .paymentMethod("CREDIT_CARD")
            .shippingAddress("123 Main St")
            .build();

        OrderResponse expectedResponse = OrderResponse.builder()
            .id(1L)
            .orderNumber("ORD-20260623-123456")
            .userId(1L)
            .status("PENDING")
            .total(new BigDecimal("199.98"))
            .build();

        when(checkoutUseCase.execute(request)).thenReturn(expectedResponse);

        // When
        OrderResponse response = orderService.checkout(request);

        // Then
        assertThat(response).isEqualTo(expectedResponse);
        verify(checkoutUseCase).execute(request);
    }

    @Test
    void getOrder_WithExistingId_ShouldDelegateToUseCase() {
        // Given
        OrderResponse expectedResponse = OrderResponse.builder()
            .id(1L)
            .orderNumber("ORD-20260623-123456")
            .status("PENDING")
            .build();

        when(getOrderUseCase.execute(1L)).thenReturn(expectedResponse);

        // When
        OrderResponse response = orderService.getOrder(1L);

        // Then
        assertThat(response).isEqualTo(expectedResponse);
        verify(getOrderUseCase).execute(1L);
    }

    @Test
    void getOrderByNumber_WithExistingNumber_ShouldDelegateToUseCase() {
        // Given
        OrderResponse expectedResponse = OrderResponse.builder()
            .id(1L)
            .orderNumber("ORD-20260623-123456")
            .status("PENDING")
            .build();

        when(getOrderUseCase.executeByOrderNumber("ORD-20260623-123456")).thenReturn(expectedResponse);

        // When
        OrderResponse response = orderService.getOrderByOrderNumber("ORD-20260623-123456");

        // Then
        assertThat(response).isEqualTo(expectedResponse);
        verify(getOrderUseCase).executeByOrderNumber("ORD-20260623-123456");
    }

    @Test
    void getUserOrders_WithValidUser_ShouldDelegateToUseCase() {
        // Given
        OrderListResponse expectedResponse = OrderListResponse.builder()
            .totalElements(1)
            .build();

        when(getUserOrdersUseCase.execute(1L, 0, 10)).thenReturn(expectedResponse);

        // When
        OrderListResponse response = orderService.getUserOrders(1L, 0, 10);

        // Then
        assertThat(response).isEqualTo(expectedResponse);
        verify(getUserOrdersUseCase).execute(1L, 0, 10);
    }

    @Test
    void cancelOrder_WithValidRequest_ShouldDelegateToUseCase() {
        // Given
        OrderResponse expectedResponse = OrderResponse.builder()
            .id(1L)
            .status("CANCELLED")
            .cancellationReason("Changed my mind")
            .build();

        when(cancelOrderUseCase.execute(1L, 1L, "Changed my mind")).thenReturn(expectedResponse);

        // When
        OrderResponse response = orderService.cancelOrder(1L, 1L, "Changed my mind");

        // Then
        assertThat(response).isEqualTo(expectedResponse);
        verify(cancelOrderUseCase).execute(1L, 1L, "Changed my mind");
    }

    @Test
    void updateOrderStatus_WithValidRequest_ShouldDelegateToUseCase() {
        // Given
        OrderResponse expectedResponse = OrderResponse.builder()
            .id(1L)
            .status("CONFIRMED")
            .build();

        when(updateOrderStatusUseCase.execute(1L, "CONFIRMED", 10L)).thenReturn(expectedResponse);

        // When
        OrderResponse response = orderService.updateOrderStatus(1L, "CONFIRMED", 10L);

        // Then
        assertThat(response).isEqualTo(expectedResponse);
        verify(updateOrderStatusUseCase).execute(1L, "CONFIRMED", 10L);
    }

    @Test
    void getVendorOrders_WithValidVendor_ShouldDelegateToUseCase() {
        // Given
        OrderListResponse expectedResponse = OrderListResponse.builder()
            .totalElements(5)
            .build();

        when(getVendorOrdersUseCase.execute(10L, 0, 10)).thenReturn(expectedResponse);

        // When
        OrderListResponse response = orderService.getVendorOrders(10L, 0, 10);

        // Then
        assertThat(response).isEqualTo(expectedResponse);
        verify(getVendorOrdersUseCase).execute(10L, 0, 10);
    }
}