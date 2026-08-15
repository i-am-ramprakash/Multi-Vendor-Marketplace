package com.marketplace.vendor.infrastructure.persistence.repository;

import com.marketplace.vendor.infrastructure.persistence.entity.VendorAnalyticsJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface VendorAnalyticsJpaRepository extends JpaRepository<VendorAnalyticsJpaEntity, Long> {

    Optional<VendorAnalyticsJpaEntity> findByVendorIdAndDate(Long vendorId, LocalDate date);

    List<VendorAnalyticsJpaEntity> findByVendorIdAndDateBetween(Long vendorId, LocalDate startDate, LocalDate endDate);

    List<VendorAnalyticsJpaEntity> findByVendorIdOrderByDateDesc(Long vendorId);

    List<VendorAnalyticsJpaEntity> findTop10ByVendorIdOrderByDateDesc(Long vendorId);
}