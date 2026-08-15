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
public class CategoryRevenue {
    private Long categoryId;
    private String categoryName;
    private BigDecimal totalRevenue;
    private Long totalProducts;
    private Long totalOrders;
    private BigDecimal averageOrderValue;
    private Double percentageOfTotal;
}