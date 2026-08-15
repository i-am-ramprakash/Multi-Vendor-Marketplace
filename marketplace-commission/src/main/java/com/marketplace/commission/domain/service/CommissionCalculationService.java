package com.marketplace.commission.domain.service;

import com.marketplace.commission.domain.entity.CommissionRule;

import java.math.BigDecimal;

public interface CommissionCalculationService {

    BigDecimal calculateCommission(Long vendorId, Long categoryId, BigDecimal orderAmount);

    CommissionRule findApplicableRule(Long vendorId, Long categoryId);
}