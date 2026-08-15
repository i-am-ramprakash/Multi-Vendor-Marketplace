package com.marketplace.vendor.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VendorAnalytics {

    private Long id;
    private Long vendorId;
    private LocalDate date;
    private Integer totalOrders;
    private BigDecimal totalRevenue;
    private BigDecimal totalCommission;
    private BigDecimal vendorPayout;
    private Integer totalProductsSold;
    private Integer uniqueCustomers;
    private BigDecimal averageOrderValue;
    private BigDecimal conversionRate;
    private Integer pageViews;
    private Integer uniqueVisitors;
    private Instant createdAt;

    public VendorAnalytics(Long vendorId, LocalDate date) {
        this.vendorId = vendorId;
        this.date = date;
        this.totalOrders = 0;
        this.totalRevenue = BigDecimal.ZERO;
        this.totalCommission = BigDecimal.ZERO;
        this.vendorPayout = BigDecimal.ZERO;
        this.totalProductsSold = 0;
        this.uniqueCustomers = 0;
        this.averageOrderValue = BigDecimal.ZERO;
        this.conversionRate = BigDecimal.ZERO;
        this.pageViews = 0;
        this.uniqueVisitors = 0;
    }

    public void recordOrder(BigDecimal orderAmount, BigDecimal commissionRate) {
        this.totalOrders++;
        this.totalRevenue = this.totalRevenue.add(orderAmount);
        
        BigDecimal commission = orderAmount.multiply(commissionRate).divide(new BigDecimal("100"));
        this.totalCommission = this.totalCommission.add(commission);
        
        BigDecimal payout = orderAmount.subtract(commission);
        this.vendorPayout = this.vendorPayout.add(payout);
        
        this.totalProductsSold++;
        
        if (this.totalOrders > 0) {
            this.averageOrderValue = this.totalRevenue.divide(new BigDecimal(this.totalOrders), 2, BigDecimal.ROUND_HALF_UP);
        }
    }

    public void addPageView(boolean uniqueVisitor) {
        this.pageViews++;
        if (uniqueVisitor) {
            this.uniqueVisitors++;
        }
    }

    public void updateConversionRate() {
        if (this.uniqueVisitors > 0) {
            this.conversionRate = new BigDecimal(this.totalOrders)
                .divide(new BigDecimal(this.uniqueVisitors), 4, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal("100"))
                .setScale(2, BigDecimal.ROUND_HALF_UP);
        }
    }
}
