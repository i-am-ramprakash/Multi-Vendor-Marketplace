package com.marketplace.commission.infrastructure.persistence.repository;

import com.marketplace.commission.infrastructure.persistence.entity.CommissionRecordJpaEntity;
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
public interface CommissionRecordJpaRepository extends JpaRepository<CommissionRecordJpaEntity, Long> {

    List<CommissionRecordJpaEntity> findByOrderId(Long orderId);

    List<CommissionRecordJpaEntity> findByVendorId(Long vendorId);

    Page<CommissionRecordJpaEntity> findByVendorId(Long vendorId, Pageable pageable);

    List<CommissionRecordJpaEntity> findByVendorIdAndIsSettled(Long vendorId, boolean isSettled);

    Page<CommissionRecordJpaEntity> findByVendorIdAndIsSettled(Long vendorId, boolean isSettled, Pageable pageable);

    List<CommissionRecordJpaEntity> findBySettlementId(Long settlementId);

    List<CommissionRecordJpaEntity> findByCreatedAtBetween(Instant start, Instant end);

    List<CommissionRecordJpaEntity> findByVendorIdAndCreatedAtBetween(Long vendorId, Instant start, Instant end);

    @Query("SELECT COALESCE(SUM(r.commissionAmount), 0) FROM CommissionRecordJpaEntity r WHERE r.vendorId = :vendorId")
    BigDecimal sumCommissionAmountByVendorId(@Param("vendorId") Long vendorId);

    @Query("SELECT COALESCE(SUM(r.commissionAmount), 0) FROM CommissionRecordJpaEntity r WHERE r.vendorId = :vendorId AND r.createdAt BETWEEN :start AND :end")
    BigDecimal sumCommissionAmountByVendorIdAndCreatedAtBetween(@Param("vendorId") Long vendorId, @Param("start") Instant start, @Param("end") Instant end);

    @Query("SELECT COALESCE(SUM(r.orderAmount), 0) FROM CommissionRecordJpaEntity r WHERE r.vendorId = :vendorId")
    BigDecimal sumOrderAmountByVendorId(@Param("vendorId") Long vendorId);

    @Query("SELECT COALESCE(SUM(r.orderAmount), 0) FROM CommissionRecordJpaEntity r WHERE r.vendorId = :vendorId AND r.createdAt BETWEEN :start AND :end")
    BigDecimal sumOrderAmountByVendorIdAndCreatedAtBetween(@Param("vendorId") Long vendorId, @Param("start") Instant start, @Param("end") Instant end);

    long countByVendorId(Long vendorId);

    long countByVendorIdAndIsSettled(Long vendorId, boolean isSettled);

    @Query("SELECT COALESCE(SUM(r.orderAmount), 0) FROM CommissionRecordJpaEntity r WHERE r.createdAt BETWEEN :start AND :end")
    BigDecimal sumOrderAmountByCreatedAtBetween(@Param("start") Instant start, @Param("end") Instant end);

    @Query("SELECT COALESCE(SUM(r.commissionAmount), 0) FROM CommissionRecordJpaEntity r WHERE r.createdAt BETWEEN :start AND :end")
    BigDecimal sumCommissionAmountByCreatedAtBetween(@Param("start") Instant start, @Param("end") Instant end);
}