package com.marketplace.admin.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopVendor {
    private Long vendorId;
    private String storeName;
    private String ownerName;
    private Long totalProducts;
    private Long totalOrders;
    private BigDecimal totalRevenue;
    private BigDecimal commissionPaid;
    private Double averageRating;
    private Integer rank;
    private LocalDateTime joinedAt;
}