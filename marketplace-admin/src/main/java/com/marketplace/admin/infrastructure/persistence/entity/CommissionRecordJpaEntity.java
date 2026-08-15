package com.marketplace.admin.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "commission_records")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(nullable = false)
    private String currency;

    @Column(name = "is_settled", nullable = false)
    private Boolean isSettled = false;

    @Column(name = "settled_at")
    private LocalDateTime settledAt;

    @Column(name = "settlement_id")
    private Long settlementId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}