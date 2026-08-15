package com.marketplace.commission.infrastructure.persistence.repository;

import com.marketplace.commission.domain.entity.CommissionAuditLog;
import com.marketplace.commission.domain.repository.CommissionAuditLogRepository;
import com.marketplace.commission.infrastructure.persistence.entity.CommissionAuditLogJpaEntity;
import com.marketplace.commission.infrastructure.persistence.mapper.CommissionAuditLogPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CommissionAuditLogRepositoryImpl implements CommissionAuditLogRepository {

    private final CommissionAuditLogJpaRepository jpaRepository;

    @Override
    public CommissionAuditLog save(CommissionAuditLog auditLog) {
        CommissionAuditLogJpaEntity jpa = CommissionAuditLogPersistenceMapper.toJpaEntity(auditLog);
        CommissionAuditLogJpaEntity saved = jpaRepository.save(jpa);
        return CommissionAuditLogPersistenceMapper.toDomain(saved);
    }

    @Override
    public List<CommissionAuditLog> findByVendorId(Long vendorId) {
        return jpaRepository.findByVendorId(vendorId).stream()
            .map(CommissionAuditLogPersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Page<CommissionAuditLog> findByVendorId(Long vendorId, Pageable pageable) {
        return jpaRepository.findByVendorId(vendorId, pageable)
            .map(CommissionAuditLogPersistenceMapper::toDomain);
    }

    @Override
    public List<CommissionAuditLog> findByOrderId(Long orderId) {
        return jpaRepository.findByOrderId(orderId).stream()
            .map(CommissionAuditLogPersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<CommissionAuditLog> findBySettlementId(Long settlementId) {
        return jpaRepository.findBySettlementId(settlementId).stream()
            .map(CommissionAuditLogPersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<CommissionAuditLog> findByAction(String action) {
        return jpaRepository.findByAction(action).stream()
            .map(CommissionAuditLogPersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }
}