package com.marketplace.commission.infrastructure.persistence.entity;

import com.marketplace.commission.domain.valueobject.SettlementStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "settlements", indexes = {
    @Index(name = "idx_settlements_vendor_id", columnList = "vendor_id"),
    @Index(name = "idx_settlements_status", columnList = "status"),
    @Index(name = "idx_settlements_settlement_number", columnList = "settlement_number", unique = true),
    @Index(name = "idx_settlements_vendor_status", columnList = "vendor_id, status"),
    @Index(name = "idx_settlements_created_at", columnList = "created_at")
})
@Data
@NoArgsConstructor
public class SettlementJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "settlement_number", nullable = false, unique = true, length = 50)
    private String settlementNumber;

    @Column(name = "vendor_id", nullable = false)
    private Long vendorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private SettlementStatus status;

    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "commission_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal commissionAmount;

    @Column(name = "net_payout", nullable = false, precision = 12, scale = 2)
    private BigDecimal netPayout;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Column(name = "record_count", nullable = false)
    private int recordCount;

    @Column(name = "period_start", nullable = false)
    private Instant periodStart;

    @Column(name = "period_end", nullable = false)
    private Instant periodEnd;

    @Column(name = "processed_at")
    private Instant processedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    @Column(name = "payment_reference", length = 100)
    private String paymentReference;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "failure_reason", columnDefinition = "TEXT")
    private String failureReason;

    @Column(name = "commission_record_ids", columnDefinition = "TEXT")
    private String commissionRecordIds;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;
}