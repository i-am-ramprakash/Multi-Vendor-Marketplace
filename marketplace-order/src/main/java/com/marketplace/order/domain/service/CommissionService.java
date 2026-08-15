package com.marketplace.order.domain.service;

import java.math.BigDecimal;

public interface CommissionService {

    BigDecimal calculateCommission(Long vendorId, BigDecimal amount);

    BigDecimal getCommissionRate(Long vendorId);
}