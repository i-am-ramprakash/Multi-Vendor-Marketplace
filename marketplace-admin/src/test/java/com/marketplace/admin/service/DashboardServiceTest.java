package com.marketplace.admin.service;

import com.marketplace.admin.application.dto.DashboardFilterRequest;
import com.marketplace.admin.application.dto.DashboardMetricsResponse;
import com.marketplace.admin.application.service.DashboardServiceImpl;
import com.marketplace.admin.domain.repository.DashboardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private DashboardRepository dashboardRepository;

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    @Test
    void shouldGetDashboardMetrics() {
        // Given
        when(dashboardRepository.countUsers()).thenReturn(100L);
        when(dashboardRepository.countVendors()).thenReturn(25L);
        when(dashboardRepository.countVendorsByStatus("APPROVED")).thenReturn(20L);
        when(dashboardRepository.countVendorsByStatus("PENDING")).thenReturn(5L);
        when(dashboardRepository.countProducts()).thenReturn(500L);
        when(dashboardRepository.countProductsByStatus("APPROVED")).thenReturn(450L);
        when(dashboardRepository.countProductsByStatus("PENDING_APPROVAL")).thenReturn(50L);
        when(dashboardRepository.countOrders()).thenReturn(1000L);
        when(dashboardRepository.countOrdersByStatus("PENDING")).thenReturn(10L);
        when(dashboardRepository.countOrdersByStatus("DELIVERED")).thenReturn(900L);
        when(dashboardRepository.countOrdersByStatus("CANCELLED")).thenReturn(50L);
        when(dashboardRepository.sumRevenue()).thenReturn(BigDecimal.valueOf(50000.00));
        when(dashboardRepository.sumCommissionRevenue()).thenReturn(BigDecimal.valueOf(5000.00));
        when(dashboardRepository.averageOrderValue()).thenReturn(BigDecimal.valueOf(50.00));

        DashboardFilterRequest filter = DashboardFilterRequest.builder().build();

        // When
        DashboardMetricsResponse response = dashboardService.getDashboardMetrics(filter);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getTotalUsers()).isEqualTo(100L);
        assertThat(response.getTotalVendors()).isEqualTo(25L);
        assertThat(response.getActiveVendors()).isEqualTo(20L);
        assertThat(response.getPendingVendors()).isEqualTo(5L);
        assertThat(response.getTotalProducts()).isEqualTo(500L);
        assertThat(response.getApprovedProducts()).isEqualTo(450L);
        assertThat(response.getPendingProducts()).isEqualTo(50L);
        assertThat(response.getTotalOrders()).isEqualTo(1000L);
        assertThat(response.getTotalRevenue()).isEqualByComparingTo(BigDecimal.valueOf(50000.00));
        assertThat(response.getCommissionRevenue()).isEqualByComparingTo(BigDecimal.valueOf(5000.00));
    }

    @Test
    void shouldGetTopVendors() {
        // Given
        var topVendors = java.util.List.of(
                com.marketplace.admin.domain.entity.TopVendor.builder()
                        .vendorId(1L)
                        .storeName("Top Store")
                        .totalRevenue(BigDecimal.valueOf(10000.00))
                        .build()
        );

        when(dashboardRepository.findTopVendorsByRevenue(10, 0)).thenReturn(topVendors);

        DashboardFilterRequest filter = DashboardFilterRequest.builder()
                .size(10)
                .build();

        // When
        var response = dashboardService.getTopVendors(filter);

        // Then
        assertThat(response).hasSize(1);
        assertThat(response.get(0).getStoreName()).isEqualTo("Top Store");
    }

    @Test
    void shouldGetTopProducts() {
        // Given
        var topProducts = java.util.List.of(
                com.marketplace.admin.domain.entity.TopProduct.builder()
                        .productId(1L)
                        .productName("Top Product")
                        .totalSold(100L)
                        .build()
        );

        when(dashboardRepository.findTopProductsBySales(10, 0)).thenReturn(topProducts);

        DashboardFilterRequest filter = DashboardFilterRequest.builder()
                .size(10)
                .build();

        // When
        var response = dashboardService.getTopProducts(filter);

        // Then
        assertThat(response).hasSize(1);
        assertThat(response.get(0).getProductName()).isEqualTo("Top Product");
    }
}