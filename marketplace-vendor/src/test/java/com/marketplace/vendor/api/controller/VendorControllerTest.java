package com.marketplace.vendor.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.vendor.application.dto.*;
import com.marketplace.vendor.application.service.VendorService;
import com.marketplace.vendor.domain.valueobject.VendorStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class VendorControllerTest {

    private MockMvc mockMvc;

    @Mock
    private VendorService vendorService;

    @InjectMocks
    private VendorController vendorController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private VendorResponse vendorResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(vendorController).build();

        vendorResponse = VendorResponse.builder()
            .id(1L)
            .userId(1L)
            .storeName("Fashion Paradise")
            .storeSlug("fashion-paradise")
            .contactEmail("vendor@example.com")
            .contactPhone("+1234567890")
            .fullAddress("123 Fashion Street, New York, NY, USA 10001")
            .commissionRate(new BigDecimal("10.00"))
            .status(VendorStatus.APPROVED)
            .totalProducts(0)
            .totalOrders(0)
            .totalRevenue(BigDecimal.ZERO)
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();
    }

    @Test
    void registerVendor_WithValidRequest_ShouldReturn200() throws Exception {
        VendorRegistrationRequest request = VendorRegistrationRequest.builder()
            .userId(1L)
            .storeName("Fashion Paradise")
            .storeSlug("fashion-paradise")
            .contactEmail("vendor@example.com")
            .build();

        when(vendorService.registerVendor(any(VendorRegistrationRequest.class))).thenReturn(vendorResponse);

        mockMvc.perform(post("/v1/vendors/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.storeName").value("Fashion Paradise"))
            .andExpect(jsonPath("$.storeSlug").value("fashion-paradise"));

        verify(vendorService).registerVendor(any(VendorRegistrationRequest.class));
    }

    @Test
    void getVendorProfile_WithValidId_ShouldReturn200() throws Exception {
        when(vendorService.getVendorProfile(1L)).thenReturn(vendorResponse);

        mockMvc.perform(get("/v1/vendors/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.storeName").value("Fashion Paradise"));

        verify(vendorService).getVendorProfile(1L);
    }

    @Test
    void getVendorByStoreSlug_WithValidSlug_ShouldReturn200() throws Exception {
        when(vendorService.getVendorProfileByStoreSlug("fashion-paradise")).thenReturn(vendorResponse);

        mockMvc.perform(get("/v1/vendors/store/fashion-paradise"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.storeSlug").value("fashion-paradise"));

        verify(vendorService).getVendorProfileByStoreSlug("fashion-paradise");
    }

    @Test
    void updateProfile_WithValidRequest_ShouldReturn200() throws Exception {
        UpdateVendorProfileRequest request = UpdateVendorProfileRequest.builder()
            .storeName("Updated Store Name")
            .build();

        VendorResponse updatedResponse = VendorResponse.builder()
            .id(1L)
            .storeName("Updated Store Name")
            .build();

        when(vendorService.updateVendorProfile(eq(1L), any(UpdateVendorProfileRequest.class))).thenReturn(updatedResponse);

        mockMvc.perform(put("/v1/vendors/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.storeName").value("Updated Store Name"));

        verify(vendorService).updateVendorProfile(eq(1L), any(UpdateVendorProfileRequest.class));
    }

    @Test
    void getVendorDashboard_WithValidId_ShouldReturn200() throws Exception {
        VendorDashboardResponse dashboardResponse = VendorDashboardResponse.builder()
            .vendor(vendorResponse)
            .todaySales(VendorDashboardResponse.SalesSummary.builder().totalOrders(0).totalRevenue(BigDecimal.ZERO).build())
            .weekSales(VendorDashboardResponse.SalesSummary.builder().totalOrders(0).totalRevenue(BigDecimal.ZERO).build())
            .monthSales(VendorDashboardResponse.SalesSummary.builder().totalOrders(0).totalRevenue(BigDecimal.ZERO).build())
            .recentOrders(List.of())
            .topProducts(List.of())
            .salesTrend(List.of())
            .build();

        when(vendorService.getVendorDashboard(1L)).thenReturn(dashboardResponse);

        mockMvc.perform(get("/v1/vendors/1/dashboard"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.vendor.storeName").value("Fashion Paradise"));

        verify(vendorService).getVendorDashboard(1L);
    }

    @Test
    void getVendorAnalytics_WithValidIdAndPeriod_ShouldReturn200() throws Exception {
        VendorAnalyticsResponse analyticsResponse = VendorAnalyticsResponse.builder()
            .vendorId(1L)
            .period("month")
            .totalOrders(0)
            .totalRevenue(BigDecimal.ZERO)
            .build();

        when(vendorService.getVendorAnalytics(1L, "month")).thenReturn(analyticsResponse);

        mockMvc.perform(get("/v1/vendors/1/analytics")
                .param("period", "month"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.vendorId").value(1))
            .andExpect(jsonPath("$.period").value("month"));

        verify(vendorService).getVendorAnalytics(1L, "month");
    }
}