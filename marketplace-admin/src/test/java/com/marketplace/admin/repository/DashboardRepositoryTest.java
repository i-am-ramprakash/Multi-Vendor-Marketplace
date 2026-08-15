package com.marketplace.admin.repository;

import com.marketplace.admin.infrastructure.persistence.repository.DashboardRepositoryImpl;
import com.marketplace.admin.infrastructure.persistence.repository.UserJpaRepository;
import com.marketplace.admin.infrastructure.persistence.repository.VendorJpaRepository;
import com.marketplace.admin.infrastructure.persistence.repository.ProductJpaRepository;
import com.marketplace.admin.infrastructure.persistence.repository.OrderJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardRepositoryTest {

    @Mock
    private UserJpaRepository userJpaRepository;

    @Mock
    private VendorJpaRepository vendorJpaRepository;

    @Mock
    private ProductJpaRepository productJpaRepository;

    @Mock
    private OrderJpaRepository orderJpaRepository;

    @InjectMocks
    private DashboardRepositoryImpl dashboardRepository;

    @Test
    void shouldCountUsers() {
        // Given
        when(userJpaRepository.countAllUsers()).thenReturn(100L);

        // When
        Long count = dashboardRepository.countUsers();

        // Then
        assertThat(count).isEqualTo(100L);
    }

    @Test
    void shouldCountVendors() {
        // Given
        when(vendorJpaRepository.countAllVendors()).thenReturn(25L);

        // When
        Long count = dashboardRepository.countVendors();

        // Then
        assertThat(count).isEqualTo(25L);
    }

    @Test
    void shouldCountProducts() {
        // Given
        when(productJpaRepository.countAllProducts()).thenReturn(500L);

        // When
        Long count = dashboardRepository.countProducts();

        // Then
        assertThat(count).isEqualTo(500L);
    }

    @Test
    void shouldCountOrders() {
        // Given
        when(orderJpaRepository.countAllOrders()).thenReturn(1000L);

        // When
        Long count = dashboardRepository.countOrders();

        // Then
        assertThat(count).isEqualTo(1000L);
    }

    @Test
    void shouldSumRevenue() {
        // Given
        when(orderJpaRepository.sumTotalRevenue()).thenReturn(BigDecimal.valueOf(50000.00));

        // When
        BigDecimal revenue = dashboardRepository.sumRevenue();

        // Then
        assertThat(revenue).isEqualByComparingTo(BigDecimal.valueOf(50000.00));
    }

    @Test
    void shouldCountVendorsByStatus() {
        // Given
        when(vendorJpaRepository.countByStatus("APPROVED")).thenReturn(20L);

        // When
        Long count = dashboardRepository.countVendorsByStatus("APPROVED");

        // Then
        assertThat(count).isEqualTo(20L);
    }

    @Test
    void shouldCountProductsByStatus() {
        // Given
        when(productJpaRepository.countByStatus("APPROVED")).thenReturn(450L);

        // When
        Long count = dashboardRepository.countProductsByStatus("APPROVED");

        // Then
        assertThat(count).isEqualTo(450L);
    }
}