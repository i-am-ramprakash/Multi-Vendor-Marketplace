package com.marketplace.commission.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "commission_audit_logs", indexes = {
    @Index(name = "idx_commission_audit_logs_vendor_id", columnList = "vendor_id"),
    @Index(name = "idx_commission_audit_logs_order_id", columnList = "order_id"),
    @Index(name = "idx_commission_audit_logs_settlement_id", columnList = "settlement_id"),
    @Index(name = "idx_commission_audit_logs_action", columnList = "action")
})
@Data
@NoArgsConstructor
public class CommissionAuditLogJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vendor_id")
    private Long vendorId;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "commission_record_id")
    private Long commissionRecordId;

    @Column(name = "settlement_id")
    private Long settlementId;

    @Column(name = "action", nullable = false, length = 50)
    private String action;

    @Column(name = "details", columnDefinition = "TEXT")
    private String details;

    @Column(name = "performed_by")
    private Long performedBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}