package com.marketplace.commission.infrastructure.persistence.repository;

import com.marketplace.commission.domain.entity.Settlement;
import com.marketplace.commission.domain.repository.SettlementRepository;
import com.marketplace.commission.domain.valueobject.SettlementStatus;
import com.marketplace.commission.infrastructure.persistence.entity.SettlementJpaEntity;
import com.marketplace.commission.infrastructure.persistence.mapper.SettlementPersistenceMapper;
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
public class SettlementRepositoryImpl implements SettlementRepository {

    private final SettlementJpaRepository jpaRepository;

    @Override
    public Settlement save(Settlement settlement) {
        SettlementJpaEntity jpa = SettlementPersistenceMapper.toJpaEntity(settlement);
        SettlementJpaEntity saved = jpaRepository.save(jpa);
        return SettlementPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<Settlement> findById(Long id) {
        return jpaRepository.findById(id)
            .map(SettlementPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Settlement> findBySettlementNumber(String settlementNumber) {
        return jpaRepository.findBySettlementNumber(settlementNumber)
            .map(SettlementPersistenceMapper::toDomain);
    }

    @Override
    public List<Settlement> findByVendorId(Long vendorId) {
        return jpaRepository.findByVendorId(vendorId).stream()
            .map(SettlementPersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Page<Settlement> findByVendorId(Long vendorId, Pageable pageable) {
        return jpaRepository.findByVendorId(vendorId, pageable)
            .map(SettlementPersistenceMapper::toDomain);
    }

    @Override
    public List<Settlement> findByVendorIdAndStatus(Long vendorId, SettlementStatus status) {
        return jpaRepository.findByVendorIdAndStatus(vendorId, status).stream()
            .map(SettlementPersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Page<Settlement> findByVendorIdAndStatus(Long vendorId, SettlementStatus status, Pageable pageable) {
        return jpaRepository.findByVendorIdAndStatus(vendorId, status, pageable)
            .map(SettlementPersistenceMapper::toDomain);
    }

    @Override
    public List<Settlement> findByStatus(SettlementStatus status) {
        return jpaRepository.findByStatus(status).stream()
            .map(SettlementPersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Page<Settlement> findByStatus(SettlementStatus status, Pageable pageable) {
        return jpaRepository.findByStatus(status, pageable)
            .map(SettlementPersistenceMapper::toDomain);
    }

    @Override
    public List<Settlement> findByPeriodStartAndPeriodEnd(Instant start, Instant end) {
        return jpaRepository.findByPeriodStartAndPeriodEnd(start, end).stream()
            .map(SettlementPersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public BigDecimal sumNetPayoutByVendorId(Long vendorId) {
        return jpaRepository.sumNetPayoutByVendorId(vendorId);
    }

    @Override
    public BigDecimal sumNetPayoutByVendorIdAndStatus(Long vendorId, SettlementStatus status) {
        return jpaRepository.sumNetPayoutByVendorIdAndStatus(vendorId, status);
    }

    @Override
    public long countByVendorId(Long vendorId) {
        return jpaRepository.countByVendorId(vendorId);
    }

    @Override
    public long countByVendorIdAndStatus(Long vendorId, SettlementStatus status) {
        return jpaRepository.countByVendorIdAndStatus(vendorId, status);
    }
}