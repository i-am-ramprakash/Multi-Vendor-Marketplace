package com.marketplace.commission.application.service;

import com.marketplace.commission.application.dto.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public interface CommissionService {

    CommissionRuleResponse createCommissionRule(CreateCommissionRuleRequest request);

    CommissionRecordResponse calculateCommission(Long orderId, Long orderItemId, Long vendorId,
                                                 Long categoryId, BigDecimal orderAmount, String currency);

    VendorEarningsResponse getVendorEarnings(Long vendorId);

    VendorEarningsResponse getVendorEarnings(Long vendorId, Instant start, Instant end);

    List<CommissionRecordResponse> getVendorRecords(Long vendorId, int page, int size);

    List<CommissionRecordResponse> getVendorUnsettledRecords(Long vendorId);

    SettlementResponse createSettlement(Long vendorId, Instant periodStart, Instant periodEnd);

    SettlementResponse processSettlement(Long settlementId, Long performedBy);

    SettlementResponse completeSettlement(Long settlementId, String paymentReference, Long performedBy);

    MonthlyRevenueResponse getMonthlyRevenue(int year, int month);
}