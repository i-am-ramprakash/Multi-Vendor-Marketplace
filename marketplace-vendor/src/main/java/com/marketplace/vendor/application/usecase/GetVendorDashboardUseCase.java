package com.marketplace.vendor.application.usecase;

import com.marketplace.vendor.application.dto.VendorDashboardResponse;
import com.marketplace.vendor.application.dto.VendorResponse;
import com.marketplace.vendor.domain.entity.Vendor;
import com.marketplace.vendor.domain.exception.VendorNotFoundException;
import com.marketplace.vendor.domain.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetVendorDashboardUseCase {

    private final VendorRepository vendorRepository;

    @Transactional(readOnly = true)
    public VendorDashboardResponse execute(Long vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId)
            .orElseThrow(() -> new VendorNotFoundException(vendorId));

        VendorResponse vendorResponse = VendorResponse.from(vendor);

        // Create sample dashboard data (in production, this would come from analytics tables)
        VendorDashboardResponse.SalesSummary todaySales = VendorDashboardResponse.SalesSummary.builder()
            .totalOrders(0)
            .totalRevenue(BigDecimal.ZERO)
            .totalCommission(BigDecimal.ZERO)
            .vendorPayout(BigDecimal.ZERO)
            .averageOrderValue(BigDecimal.ZERO)
            .build();

        VendorDashboardResponse.SalesSummary weekSales = VendorDashboardResponse.SalesSummary.builder()
            .totalOrders(0)
            .totalRevenue(BigDecimal.ZERO)
            .totalCommission(BigDecimal.ZERO)
            .vendorPayout(BigDecimal.ZERO)
            .averageOrderValue(BigDecimal.ZERO)
            .build();

        VendorDashboardResponse.SalesSummary monthSales = VendorDashboardResponse.SalesSummary.builder()
            .totalOrders(vendor.getTotalOrders())
            .totalRevenue(vendor.getTotalRevenue())
            .totalCommission(vendor.getTotalRevenue().multiply(vendor.getCommissionRate()).divide(new BigDecimal("100")))
            .vendorPayout(vendor.getTotalRevenue().subtract(vendor.getTotalRevenue().multiply(vendor.getCommissionRate()).divide(new BigDecimal("100"))))
            .averageOrderValue(vendor.getTotalOrders() > 0 ? vendor.getTotalRevenue().divide(new BigDecimal(vendor.getTotalOrders()), 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO)
            .build();

        List<VendorDashboardResponse.OrderSummary> recentOrders = new ArrayList<>();

        List<VendorDashboardResponse.ProductSummary> topProducts = new ArrayList<>();

        List<VendorDashboardResponse.SalesTrend> salesTrend = generateSalesTrend();

        return VendorDashboardResponse.builder()
            .vendor(vendorResponse)
            .todaySales(todaySales)
            .weekSales(weekSales)
            .monthSales(monthSales)
            .recentOrders(recentOrders)
            .topProducts(topProducts)
            .salesTrend(salesTrend)
            .build();
    }

    private List<VendorDashboardResponse.SalesTrend> generateSalesTrend() {
        List<VendorDashboardResponse.SalesTrend> trend = new ArrayList<>();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (int i = 29; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            trend.add(VendorDashboardResponse.SalesTrend.builder()
                .date(date.format(formatter))
                .orders(0)
                .revenue(BigDecimal.ZERO)
                .build());
        }

        return trend;
    }
}