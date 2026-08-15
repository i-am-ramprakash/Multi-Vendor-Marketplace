package com.marketplace.admin.domain.repository;

import com.marketplace.admin.domain.entity.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DashboardRepository {

    // Core Metrics
    Long countUsers();
    Long countVendors();
    Long countVendorsByStatus(String status);
    Long countProducts();
    Long countProductsByStatus(String status);
    Long countOrders();
    Long countOrdersByStatus(String status);
    BigDecimal sumRevenue();
    BigDecimal sumCommissionRevenue();
    BigDecimal averageOrderValue();

    // Time-based Metrics
    BigDecimal sumRevenueByDateRange(LocalDate from, LocalDate to);
    BigDecimal sumCommissionByDateRange(LocalDate from, LocalDate to);
    Long countOrdersByDateRange(LocalDate from, LocalDate to);
    Long countUsersByDateRange(LocalDate from, LocalDate to);

    // Top Vendors
    List<TopVendor> findTopVendorsByRevenue(int limit, int offset);
    List<TopVendor> findTopVendorsByOrders(int limit, int offset);
    List<TopVendor> findTopVendorsByRating(int limit, int offset);
    List<TopVendor> findTopVendors(LocalDate from, LocalDate to, String sortBy, int limit, int offset);

    // Top Products
    List<TopProduct> findTopProductsBySales(int limit, int offset);
    List<TopProduct> findTopProductsByRevenue(int limit, int offset);
    List<TopProduct> findTopProductsByRating(int limit, int offset);
    List<TopProduct> findTopProducts(LocalDate from, LocalDate to, Long categoryId, String sortBy, int limit, int offset);

    // Revenue Analytics
    List<DailyRevenue> findDailyRevenue(LocalDate from, LocalDate to);
    List<DailyRevenue> findWeeklyRevenue(LocalDate from, LocalDate to);
    List<DailyRevenue> findMonthlyRevenue(int months);
    List<CategoryRevenue> findCategoryRevenue(LocalDate from, LocalDate to);

    // Order Analytics
    List<OrderStatusBreakdown> findOrderStatusBreakdown(LocalDate from, LocalDate to);

    // User Analytics
    List<UserRegistrationTrend> findUserRegistrationTrend(int months);

    // Vendor Performance
    Optional<TopVendor> findVendorPerformance(Long vendorId, LocalDate from, LocalDate to);
}