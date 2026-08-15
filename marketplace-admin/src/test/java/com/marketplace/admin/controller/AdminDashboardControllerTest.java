package com.marketplace.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.admin.api.controller.AdminDashboardController;
import com.marketplace.admin.application.dto.DashboardMetricsResponse;
import com.marketplace.admin.application.dto.TopVendorResponse;
import com.marketplace.admin.application.dto.TopProductResponse;
import com.marketplace.admin.application.service.DashboardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.bean.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResul;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.test.mock.mockito.MockBean;
@WebMvcTest(AdminDashboardController.class)
class AdminDashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DashboardService dashboardService;

    private DashboardMetricsResponse metricsResponse;

    @BeforeEach
    void setUp() {
        metricsResponse = DashboardMetricsResponse.builder()
                .totalUsers(100L)
                .totalVendors(25L)
                .activeVendors(20L)
                .totalProducts(500L)
                .totalOrders(1000L)
                .totalRevenue(BigDecimal.valueOf(50000.00))
                .commissionRevenue(BigDecimal.valueOf(5000.00))
                .build();
    }

    @Test
    void shouldGetDashboardMetrics() throws Exception {
        // Given
        when(dashboardService.getDashboardMetrics(null)).thenReturn(metricsResponse);

        // When & Then
        mockMvc.perform(get("/v1/admin/dashboard/metrics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalUsers").value(100))
                .andExpect(jsonPath("$.totalVendors").value(25))
                .andExpect(jsonPath("$.totalRevenue").value(50000.00));
    }

    @Test
    void shouldGetTopVendors() throws Exception {
        // Given
        TopVendorResponse vendor = TopVendorResponse.builder()
                .vendorId(1L)
                .storeName("Top Store")
                .totalRevenue(BigDecimal.valueOf(10000.00))
                .build();

        when(dashboardService.getTopVendors(null)).thenReturn(Arrays.asList(vendor));

        // When & Then
        mockMvc.perform(get("/v1/admin/dashboard/top-vendors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].storeName").value("Top Store"));
    }

    @Test
    void shouldGetTopProducts() throws Exception {
        // Given
        TopProductResponse product = TopProductResponse.builder()
                .productId(1L)
                .productName("Top Product")
                .totalSold(100L)
                .build();

        when(dashboardService.getTopProducts(null)).thenReturn(Arrays.asList(product));

        // When & Then
        mockMvc.perform(get("/v1/admin/dashboard/top-products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].productName").value("Top Product"));
    }

    @Test
    void shouldGetDashboardSummary() throws Exception {
        // Given
        when(dashboardService.getDashboardMetrics(null)).thenReturn(metricsResponse);

        // When & Then
        mockMvc.perform(get("/v1/admin/dashboard/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalUsers").value(100));
    }
}