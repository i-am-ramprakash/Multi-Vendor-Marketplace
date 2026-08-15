package com.marketplace.order.infrastructure.service;

import com.marketplace.order.domain.service.CommissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommissionServiceImpl implements CommissionService {

    private static final BigDecimal DEFAULT_COMMISSION_RATE = new BigDecimal("10.00");

    @Override
    public BigDecimal calculateCommission(Long vendorId, BigDecimal amount) {
        BigDecimal rate = getCommissionRate(vendorId);
        return amount.multiply(rate).divide(new BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getCommissionRate(Long vendorId) {
        // TODO: Integrate with Vendor Service via Feign/RestTemplate
        log.debug("Getting commission rate for vendor: {}", vendorId);
        return DEFAULT_COMMISSION_RATE;
    }
}