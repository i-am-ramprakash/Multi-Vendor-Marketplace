package com.marketplace.commission.infrastructure.persistence.repository;

import com.marketplace.commission.infrastructure.persistence.entity.CommissionAuditLogJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommissionAuditLogJpaRepository extends JpaRepository<CommissionAuditLogJpaEntity, Long> {

    List<CommissionAuditLogJpaEntity> findByVendorId(Long vendorId);

    Page<CommissionAuditLogJpaEntity> findByVendorId(Long vendorId, Pageable pageable);

    List<CommissionAuditLogJpaEntity> findByOrderId(Long orderId);

    List<CommissionAuditLogJpaEntity> findBySettlementId(Long settlementId);

    List<CommissionAuditLogJpaEntity> findByAction(String action);
}