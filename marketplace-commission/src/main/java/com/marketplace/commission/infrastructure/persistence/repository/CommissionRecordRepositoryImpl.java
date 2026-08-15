package com.marketplace.commission.infrastructure.persistence.repository;

import com.marketplace.commission.domain.entity.CommissionRecord;
import com.marketplace.commission.domain.repository.CommissionRecordRepository;
import com.marketplace.commission.infrastructure.persistence.entity.CommissionRecordJpaEntity;
import com.marketplace.commission.infrastructure.persistence.mapper.CommissionRecordPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CommissionRecordRepositoryImpl implements CommissionRecordRepository {

    private final CommissionRecordJpaRepository jpaRepository;

    @Override
    public CommissionRecord save(CommissionRecord record) {
        CommissionRecordJpaEntity jpa = CommissionRecordPersistenceMapper.toJpaEntity(record);
        CommissionRecordJpaEntity saved = jpaRepository.save(jpa);
        return CommissionRecordPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<CommissionRecord> findById(Long id) {
        return jpaRepository.findById(id)
            .map(CommissionRecordPersistenceMapper::toDomain);
    }

    @Override
    public List<CommissionRecord> findByOrderId(Long orderId) {
        return jpaRepository.findByOrderId(orderId).stream()
            .map(CommissionRecordPersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<CommissionRecord> findByVendorId(Long vendorId) {
        return jpaRepository.findByVendorId(vendorId).stream()
            .map(CommissionRecordPersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Page<CommissionRecord> findByVendorId(Long vendorId, Pageable pageable) {
        return jpaRepository.findByVendorId(vendorId, pageable)
            .map(CommissionRecordPersistenceMapper::toDomain);
    }

    @Override
    public List<CommissionRecord> findByVendorIdAndIsSettled(Long vendorId, boolean isSettled) {
        return jpaRepository.findByVendorIdAndIsSettled(vendorId, isSettled).stream()
            .map(CommissionRecordPersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Page<CommissionRecord> findByVendorIdAndIsSettled(Long vendorId, boolean isSettled, Pageable pageable) {
        return jpaRepository.findByVendorIdAndIsSettled(vendorId, isSettled, pageable)
            .map(CommissionRecordPersistenceMapper::toDomain);
    }

    @Override
    public List<CommissionRecord> findBySettlementId(Long settlementId) {
        return jpaRepository.findBySettlementId(settlementId).stream()
            .map(CommissionRecordPersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<CommissionRecord> findByCreatedAtBetween(Instant start, Instant end) {
        return jpaRepository.findByCreatedAtBetween(start, end).stream()
            .map(CommissionRecordPersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<CommissionRecord> findByVendorIdAndCreatedAtBetween(Long vendorId, Instant start, Instant end) {
        return jpaRepository.findByVendorIdAndCreatedAtBetween(vendorId, start, end).stream()
            .map(CommissionRecordPersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public BigDecimal sumCommissionAmountByCreatedAtBetween(Instant start, Instant end) {
        return jpaRepository.sumCommissionAmountByCreatedAtBetween(start, end);
    }

    @Override
    public BigDecimal sumOrderAmountByCreatedAtBetween(Instant start, Instant end) {
        return jpaRepository.sumOrderAmountByCreatedAtBetween(start, end);
    }

    @Override
    public BigDecimal sumCommissionAmountByVendorId(Long vendorId) {
        return jpaRepository.sumCommissionAmountByVendorId(vendorId);
    }

    @Override
    public BigDecimal sumCommissionAmountByVendorIdAndCreatedAtBetween(Long vendorId, Instant start, Instant end) {
        return jpaRepository.sumCommissionAmountByVendorIdAndCreatedAtBetween(vendorId, start, end);
    }

    @Override
    public BigDecimal sumOrderAmountByVendorId(Long vendorId) {
        return jpaRepository.sumOrderAmountByVendorId(vendorId);
    }

    @Override
    public BigDecimal sumOrderAmountByVendorIdAndCreatedAtBetween(Long vendorId, Instant start, Instant end) {
        return jpaRepository.sumOrderAmountByVendorIdAndCreatedAtBetween(vendorId, start, end);
    }

    @Override
    public long countByVendorId(Long vendorId) {
        return jpaRepository.countByVendorId(vendorId);
    }

    @Override
    public long countByVendorIdAndIsSettled(Long vendorId, boolean isSettled) {
        return jpaRepository.countByVendorIdAndIsSettled(vendorId, isSettled);
    }
}