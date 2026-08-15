package com.marketplace.vendor.domain.repository;

import com.marketplace.vendor.domain.entity.VendorAnalytics;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VendorAnalyticsRepository {

    VendorAnalytics save(VendorAnalytics analytics);

    Optional<VendorAnalytics> findByVendorIdAndDate(Long vendorId, LocalDate date);

    List<VendorAnalytics> findByVendorIdAndDateBetween(Long vendorId, LocalDate startDate, LocalDate endDate);

    List<VendorAnalytics> findByVendorIdOrderByDateDesc(Long vendorId);

    List<VendorAnalytics> findTop10ByVendorIdOrderByDateDesc(Long vendorId);

    void delete(VendorAnalytics analytics);
}