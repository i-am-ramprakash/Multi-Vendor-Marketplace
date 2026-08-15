package com.marketplace.admin.domain.service;

import com.marketplace.admin.domain.entity.*;
import com.marketplace.admin.domain.valueobject.ExportFormat;

import java.time.LocalDate;
import java.util.List;

public interface DashboardDomainService {
    DashboardMetrics getDashboardMetrics();
    DashboardMetrics getDashboardMetrics(LocalDate from, LocalDate to);
    List<TopVendor> getTopVendors(int page, int size, String sortBy, LocalDate from, LocalDate to);
    List<TopProduct> getTopProducts(int page, int size, String sortBy, LocalDate from, LocalDate to, Long categoryId);
    List<DailyRevenue> getDailyRevenue(LocalDate from, LocalDate to);
    List<DailyRevenue> getMonthlyRevenue(int months);
    List<CategoryRevenue> getCategoryRevenue(LocalDate from, LocalDate to);
    List<OrderStatusBreakdown> getOrderStatusBreakdown(LocalDate from, LocalDate to);
    List<UserRegistrationTrend> getUserRegistrationTrend(int months);
    TopVendor getVendorPerformance(Long vendorId, LocalDate from, LocalDate to);
}