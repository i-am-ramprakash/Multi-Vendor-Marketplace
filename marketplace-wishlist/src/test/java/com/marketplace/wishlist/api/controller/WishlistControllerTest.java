package com.marketplace.wishlist.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.wishlist.application.dto.*;
import com.marketplace.wishlist.application.service.WishlistService;
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

@WebMvcTest(WishlistController.class)
class WishlistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private WishlistService wishlistService;

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void addItem_WithValidRequest_ShouldReturn200() throws Exception {
        // Given
        AddToWishlistRequest request = AddToWishlistRequest.builder()
            .productId(100L)
            .productName("Wireless Headphones")
            .unitPrice(new BigDecimal("99.99"))
            .build();

        WishlistItemResponse response = WishlistItemResponse.builder()
            .id(1L)
            .productId(100L)
            .productName("Wireless Headphones")
            .unitPrice(new BigDecimal("99.99"))
            .build();

        when(wishlistService.addToWishlist(any(Long.class), any(AddToWishlistRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/v1/wishlist/items")
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
        AddToWishlistRequest request = AddToWishlistRequest.builder()
            .productId(100L)
            .productName("Wireless Headphones")
            .unitPrice(new BigDecimal("99.99"))
            .build();

        // When & Then
        mockMvc.perform(post("/v1/wishlist/items")
                .param("userId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isForbidden());
    }

    @Test
    void addItem_WithoutAuth_ShouldReturn401() throws Exception {
        // Given
        AddToWishlistRequest request = AddToWishlistRequest.builder()
            .productId(100L)
            .productName("Wireless Headphones")
            .unitPrice(new BigDecimal("99.99"))
            .build();

        // When & Then
        mockMvc.perform(post("/v1/wishlist/items")
                .param("userId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void getWishlist_WithValidUser_ShouldReturn200() throws Exception {
        // Given
        WishlistResponse response = WishlistResponse.builder()
            .id(1L)
            .userId(1L)
            .name("My Wishlist")
            .itemCount(2)
            .build();

        when(wishlistService.getWishlist(1L)).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/v1/wishlist")
                .param("userId", "1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("My Wishlist"));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void clearWishlist_WithValidUser_ShouldReturn200() throws Exception {
        // When & Then
        mockMvc.perform(delete("/v1/wishlist")
                .param("userId", "1"))
            .andExpect(status().isOk());
    }
}