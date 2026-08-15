package com.marketplace.admin.application.service;

import com.marketplace.admin.application.dto.*;

import java.util.List;

public interface DashboardService {
    DashboardMetricsResponse getDashboardMetrics(DashboardFilterRequest filter);
    List<TopVendorResponse> getTopVendors(DashboardFilterRequest filter);
    List<TopProductResponse> getTopProducts(DashboardFilterRequest filter);
    RevenueAnalyticsResponse getRevenueAnalytics(DashboardFilterRequest filter);
    UserAnalyticsResponse getUserAnalytics(DashboardFilterRequest filter);
    VendorAnalyticsResponse getVendorAnalytics(DashboardFilterRequest filter);
    ProductAnalyticsResponse getProductAnalytics(DashboardFilterRequest filter);
    TopVendorResponse getVendorPerformance(Long vendorId, DashboardFilterRequest filter);
    ExportJobResponse exportReport(ExportRequest request);
    byte[] getExportData(String jobId);
}