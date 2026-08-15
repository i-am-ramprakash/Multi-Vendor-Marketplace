package com.marketplace.order.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.order.application.dto.*;
import com.marketplace.order.application.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void checkout_WithValidRequest_ShouldReturn200() throws Exception {
        // Given
        CheckoutRequest request = CheckoutRequest.builder()
            .userId(1L)
            .paymentMethod("CREDIT_CARD")
            .shippingAddress("123 Main St, New York, NY 10001")
            .items(java.util.List.of(
                CheckoutRequest.CheckoutItemRequest.builder()
                    .productId(100L)
                    .vendorId(10L)
                    .productName("Wireless Headphones")
                    .unitPrice(new BigDecimal("99.99"))
                    .quantity(2)
                    .build()
            ))
            .build();

        OrderResponse response = OrderResponse.builder()
            .id(1L)
            .orderNumber("ORD-20260623-123456")
            .userId(1L)
            .status("PENDING")
            .total(new BigDecimal("199.98"))
            .itemCount(1)
            .build();

        when(orderService.checkout(any(CheckoutRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/v1/orders/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    @WithMockUser(roles = "VENDOR")
    void checkout_WithVendorRole_ShouldReturn403() throws Exception {
        // Given
        CheckoutRequest request = CheckoutRequest.builder()
            .userId(1L)
            .paymentMethod("CREDIT_CARD")
            .shippingAddress("123 Main St")
            .build();

        // When & Then
        mockMvc.perform(post("/v1/orders/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isForbidden());
    }

    @Test
    void checkout_WithoutAuth_ShouldReturn401() throws Exception {
        // Given
        CheckoutRequest request = CheckoutRequest.builder()
            .userId(1L)
            .paymentMethod("CREDIT_CARD")
            .shippingAddress("123 Main St")
            .build();

        // When & Then
        mockMvc.perform(post("/v1/orders/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void getOrder_WithValidId_ShouldReturn200() throws Exception {
        // Given
        OrderResponse response = OrderResponse.builder()
            .id(1L)
            .orderNumber("ORD-20260623-123456")
            .status("PENDING")
            .total(new BigDecimal("199.98"))
            .build();

        when(orderService.getOrder(1L)).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/v1/orders/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.orderNumber").value("ORD-20260623-123456"));
    }

    @Test
    @WithMockUser(roles = "VENDOR")
    void updateOrderStatus_WithValidRequest_ShouldReturn200() throws Exception {
        // Given
        OrderResponse response = OrderResponse.builder()
            .id(1L)
            .status("CONFIRMED")
            .build();

        when(orderService.updateOrderStatus(1L, "CONFIRMED", 10L)).thenReturn(response);

        // When & Then
        mockMvc.perform(put("/v1/orders/1/status")
                .param("status", "CONFIRMED")
                .param("performedBy", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void cancelOrder_WithValidRequest_ShouldReturn200() throws Exception {
        // Given
        OrderResponse response = OrderResponse.builder()
            .id(1L)
            .status("CANCELLED")
            .cancellationReason("Changed my mind")
            .build();

        when(orderService.cancelOrder(1L, 1L, "Changed my mind")).thenReturn(response);

        // When & Then
        mockMvc.perform(put("/v1/orders/1/cancel")
                .param("userId", "1")
                .param("reason", "Changed my mind"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("CANCELLED"));
    }
}