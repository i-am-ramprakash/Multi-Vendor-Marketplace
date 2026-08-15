package com.marketplace.commission.infrastructure.persistence.entity;

import com.marketplace.commission.domain.valueobject.CommissionType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "commission_rules", indexes = {
    @Index(name = "idx_commission_rules_vendor_id", columnList = "vendor_id"),
    @Index(name = "idx_commission_rules_category_id", columnList = "category_id"),
    @Index(name = "idx_commission_rules_type", columnList = "type"),
    @Index(name = "idx_commission_rules_is_active", columnList = "is_active"),
    @Index(name = "idx_commission_rules_is_default", columnList = "is_default")
})
@Data
@NoArgsConstructor
public class CommissionRuleJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private CommissionType type;

    @Column(name = "rate", nullable = false, precision = 10, scale = 4)
    private BigDecimal rate;

    @Column(name = "fixed_amount", precision = 12, scale = 2)
    private BigDecimal fixedAmount;

    @Column(name = "min_order_amount", precision = 12, scale = 2)
    private BigDecimal minOrderAmount;

    @Column(name = "max_commission_amount", precision = 12, scale = 2)
    private BigDecimal maxCommissionAmount;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "vendor_id")
    private Long vendorId;

    @Column(name = "is_default", nullable = false)
    private boolean isDefault;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "priority", nullable = false)
    private int priority;

    @Column(name = "effective_from")
    private Instant effectiveFrom;

    @Column(name = "effective_to")
    private Instant effectiveTo;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}