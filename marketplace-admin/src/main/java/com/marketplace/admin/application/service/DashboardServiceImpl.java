package com.marketplace.admin.application.service;

import com.marketplace.admin.application.dto.*;
import com.marketplace.admin.domain.entity.*;
import com.marketplace.admin.domain.repository.DashboardRepository;
import com.marketplace.admin.domain.valueobject.ExportFormat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final DashboardRepository dashboardRepository;
    private final ExportService exportService;
    private final ConcurrentHashMap<String, byte[]> exportCache = new ConcurrentHashMap<>();

    @Override
    public DashboardMetricsResponse getDashboardMetrics(DashboardFilterRequest filter) {
        log.debug("Fetching dashboard metrics with filter: {}", filter);

        LocalDate fromDate = filter.getFromDate() != null ? filter.getFromDate() : LocalDate.now().minusMonths(1);
        LocalDate toDate = filter.getToDate() != null ? filter.getToDate() : LocalDate.now();
        YearMonth currentMonth = YearMonth.now();
        YearMonth previousMonth = currentMonth.minusMonths(1);

        BigDecimal currentMonthRevenue = dashboardRepository.sumRevenueByDateRange(
                currentMonth.atDay(1), currentMonth.atEndOfMonth());
        BigDecimal previousMonthRevenue = dashboardRepository.sumRevenueByDateRange(
                previousMonth.atDay(1), previousMonth.atEndOfMonth());

        Double growthRate = null;
        if (previousMonthRevenue != null && previousMonthRevenue.compareTo(BigDecimal.ZERO) > 0) {
            growthRate = currentMonthRevenue.subtract(previousMonthRevenue)
                    .divide(previousMonthRevenue, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .doubleValue();
        }

        return DashboardMetricsResponse.builder()
                .totalUsers(dashboardRepository.countUsers())
                .totalVendors(dashboardRepository.countVendors())
                .activeVendors(dashboardRepository.countVendorsByStatus("APPROVED"))
                .pendingVendors(dashboardRepository.countVendorsByStatus("PENDING"))
                .totalProducts(dashboardRepository.countProducts())
                .approvedProducts(dashboardRepository.countProductsByStatus("APPROVED"))
                .pendingProducts(dashboardRepository.countProductsByStatus("PENDING_APPROVAL"))
                .totalOrders(dashboardRepository.countOrders())
                .pendingOrders(dashboardRepository.countOrdersByStatus("PENDING"))
                .completedOrders(dashboardRepository.countOrdersByStatus("DELIVERED"))
                .cancelledOrders(dashboardRepository.countOrdersByStatus("CANCELLED"))
                .totalRevenue(dashboardRepository.sumRevenue())
                .commissionRevenue(dashboardRepository.sumCommissionRevenue())
                .averageOrderValue(dashboardRepository.averageOrderValue())
                .monthlyRevenue(currentMonthRevenue)
                .previousMonthRevenue(previousMonthRevenue)
                .revenueGrowthRate(growthRate)
                .newUsersThisMonth(dashboardRepository.countUsersByDateRange(
                        currentMonth.atDay(1), currentMonth.atEndOfMonth()))
                .newOrdersThisMonth(dashboardRepository.countOrdersByDateRange(
                        currentMonth.atDay(1), currentMonth.atEndOfMonth()))
                .generatedAt(LocalDateTime.now())
                .build();
    }

    @Override
    public List<TopVendorResponse> getTopVendors(DashboardFilterRequest filter) {
        log.debug("Fetching top vendors with filter: {}", filter);

        int page = filter.getPage() != null ? filter.getPage() : 0;
        int size = filter.getSize() != null ? filter.getSize() : 10;
        String sortBy = filter.getSortBy() != null ? filter.getSortBy() : "revenue";
        LocalDate fromDate = filter.getFromDate();
        LocalDate toDate = filter.getToDate();

        List<TopVendor> vendors;
        if (fromDate != null && toDate != null) {
            vendors = dashboardRepository.findTopVendors(fromDate, toDate, sortBy, size, page * size);
        } else {
            vendors = switch (sortBy.toLowerCase()) {
                case "orders" -> dashboardRepository.findTopVendorsByOrders(size, page * size);
                case "rating" -> dashboardRepository.findTopVendorsByRating(size, page * size);
                default -> dashboardRepository.findTopVendorsByRevenue(size, page * size);
            };
        }

        return vendors.stream()
                .map(v -> TopVendorResponse.builder()
                        .vendorId(v.getVendorId())
                        .storeName(v.getStoreName())
                        .ownerName(v.getOwnerName())
                        .totalProducts(v.getTotalProducts())
                        .totalOrders(v.getTotalOrders())
                        .totalRevenue(v.getTotalRevenue())
                        .commissionPaid(v.getCommissionPaid())
                        .averageRating(v.getAverageRating())
                        .rank(v.getRank())
                        .joinedAt(v.getJoinedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<TopProductResponse> getTopProducts(DashboardFilterRequest filter) {
        log.debug("Fetching top products with filter: {}", filter);

        int page = filter.getPage() != null ? filter.getPage() : 0;
        int size = filter.getSize() != null ? filter.getSize() : 10;
        String sortBy = filter.getSortBy() != null ? filter.getSortBy() : "sales";
        LocalDate fromDate = filter.getFromDate();
        LocalDate toDate = filter.getToDate();
        Long categoryId = filter.getCategoryId();

        List<TopProduct> products;
        if (fromDate != null && toDate != null) {
            products = dashboardRepository.findTopProducts(fromDate, toDate, categoryId, sortBy, size, page * size);
        } else {
            products = switch (sortBy.toLowerCase()) {
                case "revenue" -> dashboardRepository.findTopProductsByRevenue(size, page * size);
                case "rating" -> dashboardRepository.findTopProductsByRating(size, page * size);
                default -> dashboardRepository.findTopProductsBySales(size, page * size);
            };
        }

        return products.stream()
                .map(p -> TopProductResponse.builder()
                        .productId(p.getProductId())
                        .productName(p.getProductName())
                        .categoryName(p.getCategoryName())
                        .vendorName(p.getVendorName())
                        .totalSold(p.getTotalSold())
                        .totalRevenue(p.getTotalRevenue())
                        .averagePrice(p.getAveragePrice())
                        .totalReviews(p.getTotalReviews())
                        .averageRating(p.getAverageRating())
                        .rank(p.getRank())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public RevenueAnalyticsResponse getRevenueAnalytics(DashboardFilterRequest filter) {
        log.debug("Fetching revenue analytics with filter: {}", filter);

        LocalDate fromDate = filter.getFromDate() != null ? filter.getFromDate() : LocalDate.now().minusDays(30);
        LocalDate toDate = filter.getToDate() != null ? filter.getToDate() : LocalDate.now();

        List<DailyRevenue> dailyRevenue = dashboardRepository.findDailyRevenue(fromDate, toDate);
        List<CategoryRevenue> categoryRevenue = dashboardRepository.findCategoryRevenue(fromDate, toDate);
        List<OrderStatusBreakdown> orderStatusBreakdown = dashboardRepository.findOrderStatusBreakdown(fromDate, toDate);

        BigDecimal totalRevenue = dailyRevenue.stream()
                .map(DailyRevenue::getRevenue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCommission = dailyRevenue.stream()
                .map(DailyRevenue::getCommission)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long days = java.time.temporal.ChronoUnit.DAYS.between(fromDate, toDate) + 1;
        BigDecimal averageDailyRevenue = days > 0 ? totalRevenue.divide(BigDecimal.valueOf(days), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;

        Long totalOrders = orderStatusBreakdown.stream()
                .mapToLong(OrderStatusBreakdown::getCount)
                .sum();

        return RevenueAnalyticsResponse.builder()
                .totalRevenue(totalRevenue)
                .totalCommission(totalCommission)
                .averageDailyRevenue(averageDailyRevenue)
                .dailyRevenue(dailyRevenue.stream()
                        .map(dr -> RevenueAnalyticsResponse.DailyRevenueItem.builder()
                                .date(dr.getDate())
                                .revenue(dr.getRevenue())
                                .commission(dr.getCommission())
                                .orderCount(dr.getOrderCount())
                                .build())
                        .collect(Collectors.toList()))
                .categoryRevenue(categoryRevenue.stream()
                        .map(cr -> RevenueAnalyticsResponse.CategoryRevenueItem.builder()
                                .categoryId(cr.getCategoryId())
                                .categoryName(cr.getCategoryName())
                                .revenue(cr.getTotalRevenue())
                                .orderCount(cr.getTotalOrders())
                                .percentage(totalOrders > 0 ? (double) cr.getTotalOrders() / totalOrders * 100 : 0.0)
                                .build())
                        .collect(Collectors.toList()))
                .orderStatusBreakdown(orderStatusBreakdown.stream()
                        .map(os -> RevenueAnalyticsResponse.OrderStatusItem.builder()
                                .status(os.getStatus())
                                .count(os.getCount())
                                .percentage(os.getPercentage())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public UserAnalyticsResponse getUserAnalytics(DashboardFilterRequest filter) {
        log.debug("Fetching user analytics with filter: {}", filter);

        YearMonth currentMonth = YearMonth.now();
        YearMonth previousMonth = currentMonth.minusMonths(1);

        Long newUsersThisMonth = dashboardRepository.countUsersByDateRange(
                currentMonth.atDay(1), currentMonth.atEndOfMonth());
        Long newUsersLastMonth = dashboardRepository.countUsersByDateRange(
                previousMonth.atDay(1), previousMonth.atEndOfMonth());

        Double growthRate = null;
        if (newUsersLastMonth != null && newUsersLastMonth > 0) {
            growthRate = ((double) (newUsersThisMonth - newUsersLastMonth) / newUsersLastMonth) * 100;
        }

        List<UserRegistrationTrend> trend = dashboardRepository.findUserRegistrationTrend(12);

        return UserAnalyticsResponse.builder()
                .totalUsers(dashboardRepository.countUsers())
                .activeUsers(dashboardRepository.countUsers())
                .newUsersThisMonth(newUsersThisMonth)
                .newUsersLastMonth(newUsersLastMonth)
                .growthRate(growthRate)
                .registrationTrend(trend.stream()
                        .map(t -> UserAnalyticsResponse.UserTrendItem.builder()
                                .period(t.getPeriod())
                                .count(t.getCount())
                                .growthRate(t.getGrowthRate())
                                .build())
                        .collect(Collectors.toList()))
                .roleBreakdown(List.of())
                .build();
    }

    @Override
    public VendorAnalyticsResponse getVendorAnalytics(DashboardFilterRequest filter) {
        log.debug("Fetching vendor analytics with filter: {}", filter);

        YearMonth currentMonth = YearMonth.now();
        Long newVendorsThisMonth = dashboardRepository.countVendorsByStatus("APPROVED");

        List<TopVendor> topPerformers = dashboardRepository.findTopVendorsByRevenue(10, 0);

        BigDecimal totalVendorRevenue = dashboardRepository.sumRevenue();
        Long totalVendors = dashboardRepository.countVendors();
        BigDecimal averageRevenuePerVendor = totalVendors > 0
                ? totalVendorRevenue.divide(BigDecimal.valueOf(totalVendors), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        return VendorAnalyticsResponse.builder()
                .totalVendors(dashboardRepository.countVendors())
                .activeVendors(dashboardRepository.countVendorsByStatus("APPROVED"))
                .pendingVendors(dashboardRepository.countVendorsByStatus("PENDING"))
                .suspendedVendors(dashboardRepository.countVendorsByStatus("SUSPENDED"))
                .newVendorsThisMonth(newVendorsThisMonth)
                .growthRate(0.0)
                .averageVendorRating(0.0)
                .totalVendorRevenue(totalVendorRevenue)
                .averageRevenuePerVendor(averageRevenuePerVendor)
                .topPerformers(topPerformers.stream()
                        .map(v -> VendorAnalyticsResponse.VendorPerformanceItem.builder()
                                .vendorId(v.getVendorId())
                                .storeName(v.getStoreName())
                                .revenue(v.getTotalRevenue())
                                .orders(v.getTotalOrders())
                                .rating(v.getAverageRating())
                                .joinedAt(v.getJoinedAt())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public ProductAnalyticsResponse getProductAnalytics(DashboardFilterRequest filter) {
        log.debug("Fetching product analytics with filter: {}", filter);

        YearMonth currentMonth = YearMonth.now();

        List<TopProduct> topProducts = dashboardRepository.findTopProductsBySales(10, 0);

        return ProductAnalyticsResponse.builder()
                .totalProducts(dashboardRepository.countProducts())
                .approvedProducts(dashboardRepository.countProductsByStatus("APPROVED"))
                .pendingProducts(dashboardRepository.countProductsByStatus("PENDING_APPROVAL"))
                .rejectedProducts(dashboardRepository.countProductsByStatus("REJECTED"))
                .newProductsThisMonth(dashboardRepository.countProductsByStatus("APPROVED"))
                .growthRate(0.0)
                .averagePrice(BigDecimal.ZERO)
                .totalViews(0L)
                .totalUnitsSold(topProducts.stream().mapToLong(TopProduct::getTotalSold).sum())
                .categoryBreakdown(List.of())
                .build();
    }

    @Override
    public TopVendorResponse getVendorPerformance(Long vendorId, DashboardFilterRequest filter) {
        log.debug("Fetching vendor performance for vendorId: {}", vendorId);

        LocalDate fromDate = filter.getFromDate() != null ? filter.getFromDate() : LocalDate.now().minusDays(30);
        LocalDate toDate = filter.getToDate() != null ? filter.getToDate() : LocalDate.now();

        TopVendor vendor = dashboardRepository.findVendorPerformance(vendorId, fromDate, toDate)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        return TopVendorResponse.builder()
                .vendorId(vendor.getVendorId())
                .storeName(vendor.getStoreName())
                .ownerName(vendor.getOwnerName())
                .totalProducts(vendor.getTotalProducts())
                .totalOrders(vendor.getTotalOrders())
                .totalRevenue(vendor.getTotalRevenue())
                .commissionPaid(vendor.getCommissionPaid())
                .averageRating(vendor.getAverageRating())
                .rank(1)
                .joinedAt(vendor.getJoinedAt())
                .build();
    }

    @Override
    @Transactional
    public ExportJobResponse exportReport(ExportRequest request) {
        log.info("Starting export job for report: {}, format: {}", request.getReportType(), request.getFormat());

        String jobId = UUID.randomUUID().toString();
        String format = request.getFormat() != null ? request.getFormat().toUpperCase() : "CSV";

        try {
            ExportFormat exportFormat = ExportFormat.valueOf(format);
            byte[] data = switch (request.getReportType().toLowerCase()) {
                case "vendors" -> {
                    List<TopVendor> vendors = dashboardRepository.findTopVendorsByRevenue(1000, 0);
                    yield exportService.exportTopVendors(vendors, exportFormat);
                }
                case "products" -> {
                    List<TopProduct> products = dashboardRepository.findTopProductsBySales(1000, 0);
                    yield exportService.exportTopProducts(products, exportFormat);
                }
                default -> new byte[0];
            };
            exportCache.put(jobId, data);

            return ExportJobResponse.builder()
                    .jobId(jobId)
                    .reportType(request.getReportType())
                    .format(request.getFormat())
                    .status("COMPLETED")
                    .downloadUrl("/v1/admin/dashboard/export/" + jobId + "/download")
                    .totalRecords(0L)
                    .processedRecords(0L)
                    .createdAt(LocalDateTime.now())
                    .completedAt(LocalDateTime.now())
                    .build();
        } catch (Exception e) {
            log.error("Export failed: {}", e.getMessage(), e);
            return ExportJobResponse.builder()
                    .jobId(jobId)
                    .reportType(request.getReportType())
                    .format(request.getFormat())
                    .status("FAILED")
                    .createdAt(LocalDateTime.now())
                    .build();
        }
    }

    @Override
    public byte[] getExportData(String jobId) {
        byte[] data = exportCache.get(jobId);
        if (data == null) {
            throw new RuntimeException("Export job not found: " + jobId);
        }
        exportCache.remove(jobId);
        return data;
    }
}