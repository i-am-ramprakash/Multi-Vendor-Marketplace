package com.marketplace.commission.api.controller;

import com.marketplace.commission.application.dto.*;
import com.marketplace.commission.application.service.CommissionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommissionController.class)
class CommissionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommissionService commissionService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void calculateCommission_WithValidRequest_ShouldReturn200() throws Exception {
        // Given
        CommissionRecordResponse expectedResponse = CommissionRecordResponse.builder()
            .id(1L)
            .orderId(100L)
            .vendorId(10L)
            .orderAmount(new BigDecimal("100.00"))
            .commissionAmount(new BigDecimal("10.00"))
            .vendorPayout(new BigDecimal("90.00"))
            .commissionRate(new BigDecimal("10.00"))
            .currency("USD")
            .build();

        when(commissionService.calculateCommission(
            eq(100L), eq(200L), eq(10L), eq(5L), eq(new BigDecimal("100.00")), eq("USD")))
            .thenReturn(expectedResponse);

        // When & Then
        mockMvc.perform(get("/v1/commissions/calculate")
                .param("orderId", "100")
                .param("orderItemId", "200")
                .param("vendorId", "10")
                .param("categoryId", "5")
                .param("orderAmount", "100.00")
                .param("currency", "USD"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.orderId").value(100))
            .andExpect(jsonPath("$.vendorId").value(10))
            .andExpect(jsonPath("$.orderAmount").value(100.00))
            .andExpect(jsonPath("$.commissionAmount").value(10.00))
            .andExpect(jsonPath("$.vendorPayout").value(90.00));
    }

    @Test
    @WithMockUser(roles = "VENDOR")
    void getVendorEarnings_WithValidVendor_ShouldReturn200() throws Exception {
        // Given
        VendorEarningsResponse expectedResponse = VendorEarningsResponse.builder()
            .vendorId(10L)
            .totalSales(new BigDecimal("1000.00"))
            .totalCommission(new BigDecimal("100.00"))
            .totalNetEarnings(new BigDecimal("900.00"))
            .build();

        when(commissionService.getVendorEarnings(10L)).thenReturn(expectedResponse);

        // When & Then
        mockMvc.perform(get("/v1/commissions/vendor/10/earnings"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.vendorId").value(10))
            .andExpect(jsonPath("$.totalSales").value(1000.00))
            .andExpect(jsonPath("$.totalCommission").value(100.00))
            .andExpect(jsonPath("$.totalNetEarnings").value(900.00));
    }

    @Test
    @WithMockUser(roles = "VENDOR")
    void getVendorRecords_WithValidVendor_ShouldReturn200() throws Exception {
        // Given
        List<CommissionRecordResponse> expectedResponse = List.of(
            CommissionRecordResponse.builder().id(1L).orderId(100L).build(),
            CommissionRecordResponse.builder().id(2L).orderId(101L).build()
        );

        when(commissionService.getVendorRecords(10L, 0, 10)).thenReturn(expectedResponse);

        // When & Then
        mockMvc.perform(get("/v1/commissions/vendor/10/records")
                .param("page", "0")
                .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    @WithMockUser(roles = "VENDOR")
    void getVendorUnsettledRecords_WithValidVendor_ShouldReturn200() throws Exception {
        // Given
        List<CommissionRecordResponse> expectedResponse = List.of(
            CommissionRecordResponse.builder().id(1L).orderId(100L).isSettled(false).build()
        );

        when(commissionService.getVendorUnsettledRecords(10L)).thenReturn(expectedResponse);

        // When & Then
        mockMvc.perform(get("/v1/commissions/vendor/10/records/unsettled"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].isSettled").value(false));
    }
}