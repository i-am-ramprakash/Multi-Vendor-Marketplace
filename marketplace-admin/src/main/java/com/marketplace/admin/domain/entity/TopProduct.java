package com.marketplace.admin.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopProduct {
    private Long productId;
    private String productName;
    private String categoryName;
    private String vendorName;
    private Long totalSold;
    private BigDecimal totalRevenue;
    private BigDecimal averagePrice;
    private Integer totalReviews;
    private Double averageRating;
    private Integer rank;
}