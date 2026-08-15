package com.marketplace.vendor.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "vendor_analytics", indexes = {
    @Index(name = "idx_vendor_analytics_vendor_date", columnList = "vendor_id, date")
})
@Getter
@Setter
@NoArgsConstructor
public class VendorAnalyticsJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vendor_id", nullable = false)
    private Long vendorId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "total_orders", nullable = false)
    private Integer totalOrders;

    @Column(name = "total_revenue", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalRevenue;

    @Column(name = "total_commission", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalCommission;

    @Column(name = "vendor_payout", nullable = false, precision = 12, scale = 2)
    private BigDecimal vendorPayout;

    @Column(name = "total_products_sold", nullable = false)
    private Integer totalProductsSold;

    @Column(name = "unique_customers", nullable = false)
    private Integer uniqueCustomers;

    @Column(name = "average_order_value", nullable = false, precision = 12, scale = 2)
    private BigDecimal averageOrderValue;

    @Column(name = "conversion_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal conversionRate;

    @Column(name = "page_views", nullable = false)
    private Integer pageViews;

    @Column(name = "unique_visitors", nullable = false)
    private Integer uniqueVisitors;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}