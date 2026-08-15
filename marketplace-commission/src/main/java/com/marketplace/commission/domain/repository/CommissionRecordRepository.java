package com.marketplace.commission.domain.repository;

import com.marketplace.commission.domain.entity.CommissionRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface CommissionRecordRepository {

    CommissionRecord save(CommissionRecord record);

    Optional<CommissionRecord> findById(Long id);

    List<CommissionRecord> findByOrderId(Long orderId);

    List<CommissionRecord> findByVendorId(Long vendorId);

    Page<CommissionRecord> findByVendorId(Long vendorId, Pageable pageable);

    List<CommissionRecord> findByVendorIdAndIsSettled(Long vendorId, boolean isSettled);

    Page<CommissionRecord> findByVendorIdAndIsSettled(Long vendorId, boolean isSettled, Pageable pageable);

    List<CommissionRecord> findBySettlementId(Long settlementId);

    List<CommissionRecord> findByCreatedAtBetween(Instant start, Instant end);

    List<CommissionRecord> findByVendorIdAndCreatedAtBetween(Long vendorId, Instant start, Instant end);

    BigDecimal sumCommissionAmountByCreatedAtBetween(Instant start, Instant end);

    BigDecimal sumOrderAmountByCreatedAtBetween(Instant start, Instant end);

    BigDecimal sumCommissionAmountByVendorId(Long vendorId);

    BigDecimal sumCommissionAmountByVendorIdAndCreatedAtBetween(Long vendorId, Instant start, Instant end);

    BigDecimal sumOrderAmountByVendorId(Long vendorId);

    BigDecimal sumOrderAmountByVendorIdAndCreatedAtBetween(Long vendorId, Instant start, Instant end);

    long countByVendorId(Long vendorId);

    long countByVendorIdAndIsSettled(Long vendorId, boolean isSettled);
}