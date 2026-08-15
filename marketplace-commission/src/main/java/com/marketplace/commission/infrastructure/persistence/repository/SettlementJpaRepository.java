package com.marketplace.commission.infrastructure.persistence.repository;

import com.marketplace.commission.domain.valueobject.SettlementStatus;
import com.marketplace.commission.infrastructure.persistence.entity.SettlementJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface SettlementJpaRepository extends JpaRepository<SettlementJpaEntity, Long> {

    Optional<SettlementJpaEntity> findBySettlementNumber(String settlementNumber);

    List<SettlementJpaEntity> findByVendorId(Long vendorId);

    Page<SettlementJpaEntity> findByVendorId(Long vendorId, Pageable pageable);

    List<SettlementJpaEntity> findByVendorIdAndStatus(Long vendorId, SettlementStatus status);

    Page<SettlementJpaEntity> findByVendorIdAndStatus(Long vendorId, SettlementStatus status, Pageable pageable);

    List<SettlementJpaEntity> findByStatus(SettlementStatus status);

    Page<SettlementJpaEntity> findByStatus(SettlementStatus status, Pageable pageable);

    List<SettlementJpaEntity> findByPeriodStartAndPeriodEnd(Instant start, Instant end);

    @Query("SELECT COALESCE(SUM(s.netPayout), 0) FROM SettlementJpaEntity s WHERE s.vendorId = :vendorId")
    BigDecimal sumNetPayoutByVendorId(@Param("vendorId") Long vendorId);

    @Query("SELECT COALESCE(SUM(s.netPayout), 0) FROM SettlementJpaEntity s WHERE s.vendorId = :vendorId AND s.status = :status")
    BigDecimal sumNetPayoutByVendorIdAndStatus(@Param("vendorId") Long vendorId, @Param("status") SettlementStatus status);

    long countByVendorId(Long vendorId);

    long countByVendorIdAndStatus(Long vendorId, SettlementStatus status);
}