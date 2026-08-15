package com.marketplace.product.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.product.application.dto.CreateProductRequest;
import com.marketplace.product.application.dto.ProductResponse;
import com.marketplace.product.application.service.ProductService;
import com.marketplace.product.domain.valueobject.ProductStatus;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    @WithMockUser(roles = "VENDOR")
    void createProduct_WithValidRequest_ShouldReturn200() throws Exception {
        // Given
        CreateProductRequest request = CreateProductRequest.builder()
            .vendorId(1L)
            .categoryId(1L)
            .name("Wireless Headphones")
            .basePrice(new BigDecimal("99.99"))
            .build();

        ProductResponse response = ProductResponse.builder()
            .id(1L)
            .vendorId(1L)
            .name("Wireless Headphones")
            .basePrice(new BigDecimal("99.99"))
            .status(ProductStatus.DRAFT)
            .build();

        when(productService.createProduct(any(CreateProductRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Wireless Headphones"))
            .andExpect(jsonPath("$.basePrice").value(99.99));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void createProduct_WithCustomerRole_ShouldReturn403() throws Exception {
        // Given
        CreateProductRequest request = CreateProductRequest.builder()
            .vendorId(1L)
            .categoryId(1L)
            .name("Wireless Headphones")
            .basePrice(new BigDecimal("99.99"))
            .build();

        // When & Then
        mockMvc.perform(post("/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isForbidden());
    }

    @Test
    void createProduct_WithoutAuth_ShouldReturn401() throws Exception {
        // Given
        CreateProductRequest request = CreateProductRequest.builder()
            .vendorId(1L)
            .categoryId(1L)
            .name("Wireless Headphones")
            .basePrice(new BigDecimal("99.99"))
            .build();

        // When & Then
        mockMvc.perform(post("/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isUnauthorized());
    }
}