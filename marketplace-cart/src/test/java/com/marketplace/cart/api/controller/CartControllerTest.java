package com.marketplace.cart.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.cart.application.dto.*;
import com.marketplace.cart.application.service.CartService;
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

@WebMvcTest(CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CartService cartService;

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void addItem_WithValidRequest_ShouldReturn200() throws Exception {
        // Given
        AddToCartRequest request = AddToCartRequest.builder()
            .productId(100L)
            .productName("Wireless Headphones")
            .unitPrice(new BigDecimal("99.99"))
            .quantity(1)
            .build();

        CartItemResponse response = CartItemResponse.builder()
            .id(1L)
            .productId(100L)
            .productName("Wireless Headphones")
            .unitPrice(new BigDecimal("99.99"))
            .quantity(1)
            .subtotal(new BigDecimal("99.99"))
            .build();

        when(cartService.addToCart(any(Long.class), any(AddToCartRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/v1/cart/items")
                .param("userId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.productName").value("Wireless Headphones"));
    }

    @Test
    @WithMockUser(roles = "VENDOR")
    void addItem_WithVendorRole_ShouldReturn403() throws Exception {
        // Given
        AddToCartRequest request = AddToCartRequest.builder()
            .productId(100L)
            .productName("Wireless Headphones")
            .unitPrice(new BigDecimal("99.99"))
            .quantity(1)
            .build();

        // When & Then
        mockMvc.perform(post("/v1/cart/items")
                .param("userId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isForbidden());
    }

    @Test
    void addItem_WithoutAuth_ShouldReturn401() throws Exception {
        // Given
        AddToCartRequest request = AddToCartRequest.builder()
            .productId(100L)
            .productName("Wireless Headphones")
            .unitPrice(new BigDecimal("99.99"))
            .quantity(1)
            .build();

        // When & Then
        mockMvc.perform(post("/v1/cart/items")
                .param("userId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void getCart_WithValidUser_ShouldReturn200() throws Exception {
        // Given
        CartResponse response = CartResponse.builder()
            .id(1L)
            .userId(1L)
            .itemCount(2)
            .subtotal(new BigDecimal("199.98"))
            .total(new BigDecimal("199.98"))
            .build();

        when(cartService.getCart(1L)).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/v1/cart")
                .param("userId", "1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.itemCount").value(2));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void clearCart_WithValidUser_ShouldReturn200() throws Exception {
        // When & Then
        mockMvc.perform(delete("/v1/cart")
                .param("userId", "1"))
            .andExpect(status().isOk());
    }
}