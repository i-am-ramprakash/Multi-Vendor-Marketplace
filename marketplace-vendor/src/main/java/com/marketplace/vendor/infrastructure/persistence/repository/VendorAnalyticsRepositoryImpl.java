package com.marketplace.vendor.infrastructure.persistence.repository;

import com.marketplace.vendor.domain.entity.VendorAnalytics;
import com.marketplace.vendor.domain.repository.VendorAnalyticsRepository;
import com.marketplace.vendor.infrastructure.persistence.entity.VendorAnalyticsJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class VendorAnalyticsRepositoryImpl implements VendorAnalyticsRepository {

    private final VendorAnalyticsJpaRepository jpaRepository;

    @Override
    public VendorAnalytics save(VendorAnalytics analytics) {
        VendorAnalyticsJpaEntity jpa = toJpaEntity(analytics);
        VendorAnalyticsJpaEntity saved = jpaRepository.save(jpa);
        return toDomain(saved);
    }

    @Override
    public Optional<VendorAnalytics> findByVendorIdAndDate(Long vendorId, LocalDate date) {
        return jpaRepository.findByVendorIdAndDate(vendorId, date)
            .map(this::toDomain);
    }

    @Override
    public List<VendorAnalytics> findByVendorIdAndDateBetween(Long vendorId, LocalDate startDate, LocalDate endDate) {
        return jpaRepository.findByVendorIdAndDateBetween(vendorId, startDate, endDate).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<VendorAnalytics> findByVendorIdOrderByDateDesc(Long vendorId) {
        return jpaRepository.findByVendorIdOrderByDateDesc(vendorId).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<VendorAnalytics> findTop10ByVendorIdOrderByDateDesc(Long vendorId) {
        return jpaRepository.findTop10ByVendorIdOrderByDateDesc(vendorId).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public void delete(VendorAnalytics analytics) {
        jpaRepository.delete(toJpaEntity(analytics));
    }

    private VendorAnalyticsJpaEntity toJpaEntity(VendorAnalytics domain) {
        if (domain == null) return null;

        VendorAnalyticsJpaEntity jpa = new VendorAnalyticsJpaEntity();
        jpa.setId(domain.getId());
        jpa.setVendorId(domain.getVendorId());
        jpa.setDate(domain.getDate());
        jpa.setTotalOrders(domain.getTotalOrders());
        jpa.setTotalRevenue(domain.getTotalRevenue());
        jpa.setTotalCommission(domain.getTotalCommission());
        jpa.setVendorPayout(domain.getVendorPayout());
        jpa.setTotalProductsSold(domain.getTotalProductsSold());
        jpa.setUniqueCustomers(domain.getUniqueCustomers());
        jpa.setAverageOrderValue(domain.getAverageOrderValue());
        jpa.setConversionRate(domain.getConversionRate());
        jpa.setPageViews(domain.getPageViews());
        jpa.setUniqueVisitors(domain.getUniqueVisitors());
        jpa.setCreatedAt(domain.getCreatedAt());
        return jpa;
    }

    private VendorAnalytics toDomain(VendorAnalyticsJpaEntity jpa) {
        if (jpa == null) return null;

        VendorAnalytics domain = new VendorAnalytics(jpa.getVendorId(), jpa.getDate());
        
        try {
            java.lang.reflect.Field idField = VendorAnalytics.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(domain, jpa.getId());
        } catch (Exception e) {
            throw new IllegalStateException("Cannot set analytics ID", e);
        }

        domain.setTotalOrders(jpa.getTotalOrders());
        domain.setTotalRevenue(jpa.getTotalRevenue());
        domain.setTotalCommission(jpa.getTotalCommission());
        domain.setVendorPayout(jpa.getVendorPayout());
        domain.setTotalProductsSold(jpa.getTotalProductsSold());
        domain.setUniqueCustomers(jpa.getUniqueCustomers());
        domain.setAverageOrderValue(jpa.getAverageOrderValue());
        domain.setConversionRate(jpa.getConversionRate());
        domain.setPageViews(jpa.getPageViews());
        domain.setUniqueVisitors(jpa.getUniqueVisitors());
        domain.setCreatedAt(jpa.getCreatedAt());

        return domain;
    }
}