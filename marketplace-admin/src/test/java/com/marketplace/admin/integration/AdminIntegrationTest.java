package com.marketplace.admin.integration;

import com.marketplace.admin.application.dto.DashboardFilterRequest;
import com.marketplace.admin.application.dto.DashboardMetricsResponse;
import com.marketplace.admin.application.service.DashboardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class AdminIntegrationTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("multivendor_marketplace")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @Autowired
    private DashboardService dashboardService;

    @Test
    void shouldGetDashboardMetrics() {
        // When
        DashboardFilterRequest filter = DashboardFilterRequest.builder().build();
        DashboardMetricsResponse metrics = dashboardService.getDashboardMetrics(filter);

        // Then
        assertThat(metrics).isNotNull();
        assertThat(metrics.getTotalUsers()).isNotNull();
        assertThat(metrics.getTotalVendors()).isNotNull();
        assertThat(metrics.getTotalProducts()).isNotNull();
        assertThat(metrics.getTotalOrders()).isNotNull();
    }

    @Test
    void shouldGetTopVendors() {
        // When
        DashboardFilterRequest filter = DashboardFilterRequest.builder().size(10).build();
        var vendors = dashboardService.getTopVendors(filter);

        // Then
        assertThat(vendors).isNotNull();
    }

    @Test
    void shouldGetTopProducts() {
        // When
        DashboardFilterRequest filter = DashboardFilterRequest.builder().size(10).build();
        var products = dashboardService.getTopProducts(filter);

        // Then
        assertThat(products).isNotNull();
    }
}