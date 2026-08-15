package com.marketplace.commission.domain.repository;

import com.marketplace.commission.domain.entity.Settlement;
import com.marketplace.commission.domain.valueobject.SettlementStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface SettlementRepository {

    Settlement save(Settlement settlement);

    Optional<Settlement> findById(Long id);

    Optional<Settlement> findBySettlementNumber(String settlementNumber);

    List<Settlement> findByVendorId(Long vendorId);

    Page<Settlement> findByVendorId(Long vendorId, Pageable pageable);

    List<Settlement> findByVendorIdAndStatus(Long vendorId, SettlementStatus status);

    Page<Settlement> findByVendorIdAndStatus(Long vendorId, SettlementStatus status, Pageable pageable);

    List<Settlement> findByStatus(SettlementStatus status);

    Page<Settlement> findByStatus(SettlementStatus status, Pageable pageable);

    List<Settlement> findByPeriodStartAndPeriodEnd(Instant start, Instant end);

    BigDecimal sumNetPayoutByVendorId(Long vendorId);

    BigDecimal sumNetPayoutByVendorIdAndStatus(Long vendorId, SettlementStatus status);

    long countByVendorId(Long vendorId);

    long countByVendorIdAndStatus(Long vendorId, SettlementStatus status);
}