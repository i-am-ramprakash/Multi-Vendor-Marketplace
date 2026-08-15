package com.marketplace.vendor.application.usecase;

import com.marketplace.vendor.application.dto.VendorAnalyticsResponse;
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
public class GetVendorAnalyticsUseCase {

    private final VendorRepository vendorRepository;

    @Transactional(readOnly = true)
    public VendorAnalyticsResponse execute(Long vendorId, String period) {
        vendorRepository.findById(vendorId)
            .orElseThrow(() -> new VendorNotFoundException(vendorId));

        LocalDate endDate = LocalDate.now();
        LocalDate startDate;

        switch (period.toLowerCase()) {
            case "week" -> startDate = endDate.minusWeeks(1);
            case "month" -> startDate = endDate.minusMonths(1);
            case "quarter" -> startDate = endDate.minusMonths(3);
            case "year" -> startDate = endDate.minusYears(1);
            default -> startDate = endDate.minusMonths(1);
        }

        // In production, this would query the vendor_analytics table
        // For now, return sample data
        List<VendorAnalyticsResponse.TopProduct> topProducts = new ArrayList<>();

        List<VendorAnalyticsResponse.RevenueByDay> revenueByDay = generateRevenueByDay(startDate, endDate);

        List<VendorAnalyticsResponse.OrderStatusBreakdown> orderStatusBreakdown = List.of(
            VendorAnalyticsResponse.OrderStatusBreakdown.builder()
                .status("COMPLETED")
                .count(0)
                .percentage(BigDecimal.ZERO)
                .build(),
            VendorAnalyticsResponse.OrderStatusBreakdown.builder()
                .status("PENDING")
                .count(0)
                .percentage(BigDecimal.ZERO)
                .build(),
            VendorAnalyticsResponse.OrderStatusBreakdown.builder()
                .status("CANCELLED")
                .count(0)
                .percentage(BigDecimal.ZERO)
                .build()
        );

        return VendorAnalyticsResponse.builder()
            .vendorId(vendorId)
            .period(period)
            .totalOrders(0)
            .totalRevenue(BigDecimal.ZERO)
            .totalCommission(BigDecimal.ZERO)
            .vendorPayout(BigDecimal.ZERO)
            .averageOrderValue(BigDecimal.ZERO)
            .conversionRate(BigDecimal.ZERO)
            .totalPageViews(0)
            .uniqueVisitors(0)
            .topProducts(topProducts)
            .revenueByDay(revenueByDay)
            .orderStatusBreakdown(orderStatusBreakdown)
            .build();
    }

    private List<VendorAnalyticsResponse.RevenueByDay> generateRevenueByDay(LocalDate startDate, LocalDate endDate) {
        List<VendorAnalyticsResponse.RevenueByDay> revenueByDay = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate current = startDate;
        while (!current.isAfter(endDate)) {
            revenueByDay.add(VendorAnalyticsResponse.RevenueByDay.builder()
                .date(current.format(formatter))
                .revenue(BigDecimal.ZERO)
                .orders(0)
                .build());
            current = current.plusDays(1);
        }

        return revenueByDay;
    }
}