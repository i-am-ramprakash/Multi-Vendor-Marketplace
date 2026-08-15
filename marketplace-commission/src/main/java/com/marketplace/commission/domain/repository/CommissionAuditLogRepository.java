package com.marketplace.commission.domain.repository;

import com.marketplace.commission.domain.entity.CommissionAuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommissionAuditLogRepository {

    CommissionAuditLog save(CommissionAuditLog auditLog);

    List<CommissionAuditLog> findByVendorId(Long vendorId);

    Page<CommissionAuditLog> findByVendorId(Long vendorId, Pageable pageable);

    List<CommissionAuditLog> findByOrderId(Long orderId);

    List<CommissionAuditLog> findBySettlementId(Long settlementId);

    List<CommissionAuditLog> findByAction(String action);
}