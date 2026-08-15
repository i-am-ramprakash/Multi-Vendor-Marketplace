package com.marketplace.commission.application.service;

import com.marketplace.commission.application.dto.*;
import com.marketplace.commission.application.usecase.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommissionServiceImpl implements CommissionService {

    private final CreateCommissionRuleUseCase createCommissionRuleUseCase;
    private final CalculateCommissionUseCase calculateCommissionUseCase;
    private final GetVendorEarningsUseCase getVendorEarningsUseCase;
    private final CreateSettlementUseCase createSettlementUseCase;
    private final ProcessSettlementUseCase processSettlementUseCase;
    private final CompleteSettlementUseCase completeSettlementUseCase;
    private final GetMonthlyRevenueUseCase getMonthlyRevenueUseCase;

    @Override
    @Transactional
    public CommissionRuleResponse createCommissionRule(CreateCommissionRuleRequest request) {
        return createCommissionRuleUseCase.execute(request);
    }

    @Override
    @Transactional
    public CommissionRecordResponse calculateCommission(Long orderId, Long orderItemId, Long vendorId,
                                                       Long categoryId, BigDecimal orderAmount, String currency) {
        return calculateCommissionUseCase.execute(orderId, orderItemId, vendorId, categoryId, orderAmount, currency);
    }

    @Override
    @Transactional(readOnly = true)
    public VendorEarningsResponse getVendorEarnings(Long vendorId) {
        return getVendorEarningsUseCase.execute(vendorId);
    }

    @Override
    @Transactional(readOnly = true)
    public VendorEarningsResponse getVendorEarnings(Long vendorId, Instant start, Instant end) {
        return getVendorEarningsUseCase.execute(vendorId, start, end);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommissionRecordResponse> getVendorRecords(Long vendorId, int page, int size) {
        return getVendorEarningsUseCase.getVendorRecords(vendorId, page, size);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommissionRecordResponse> getVendorUnsettledRecords(Long vendorId) {
        return getVendorEarningsUseCase.getVendorUnsettledRecords(vendorId);
    }

    @Override
    @Transactional
    public SettlementResponse createSettlement(Long vendorId, Instant periodStart, Instant periodEnd) {
        return createSettlementUseCase.execute(vendorId, periodStart, periodEnd);
    }

    @Override
    @Transactional
    public SettlementResponse processSettlement(Long settlementId, Long performedBy) {
        return processSettlementUseCase.execute(settlementId, performedBy);
    }

    @Override
    @Transactional
    public SettlementResponse completeSettlement(Long settlementId, String paymentReference, Long performedBy) {
        return completeSettlementUseCase.execute(settlementId, paymentReference, performedBy);
    }

    @Override
    @Transactional(readOnly = true)
    public MonthlyRevenueResponse getMonthlyRevenue(int year, int month) {
        return getMonthlyRevenueUseCase.execute(year, month);
    }
}