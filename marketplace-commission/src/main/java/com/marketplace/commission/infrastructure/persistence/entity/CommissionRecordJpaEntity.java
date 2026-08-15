package com.marketplace.commission.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "commission_records", indexes = {
    @Index(name = "idx_commission_records_order_id", columnList = "order_id"),
    @Index(name = "idx_commission_records_vendor_id", columnList = "vendor_id"),
    @Index(name = "idx_commission_records_settlement_id", columnList = "settlement_id"),
    @Index(name = "idx_commission_records_is_settled", columnList = "is_settled"),
    @Index(name = "idx_commission_records_created_at", columnList = "created_at")
})
@Data
@NoArgsConstructor
public class CommissionRecordJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "order_item_id", nullable = false)
    private Long orderItemId;

    @Column(name = "vendor_id", nullable = false)
    private Long vendorId;

    @Column(name = "commission_rule_id", nullable = false)
    private Long commissionRuleId;

    @Column(name = "order_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal orderAmount;

    @Column(name = "commission_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal commissionAmount;

    @Column(name = "vendor_payout", nullable = false, precision = 12, scale = 2)
    private BigDecimal vendorPayout;

    @Column(name = "commission_rate", nullable = false, precision = 10, scale = 4)
    private BigDecimal commissionRate;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Column(name = "is_settled", nullable = false)
    private boolean isSettled;

    @Column(name = "settled_at")
    private Instant settledAt;

    @Column(name = "settlement_id")
    private Long settlementId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}