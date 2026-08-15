package com.marketplace.commission.application.usecase;

import com.marketplace.commission.application.dto.CommissionRecordResponse;
import com.marketplace.commission.application.dto.VendorEarningsResponse;
import com.marketplace.commission.domain.entity.CommissionRecord;
import com.marketplace.commission.domain.repository.CommissionRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetVendorEarningsUseCase {

    private final CommissionRecordRepository commissionRecordRepository;

    @Transactional(readOnly = true)
    public VendorEarningsResponse execute(Long vendorId) {
        BigDecimal totalSales = commissionRecordRepository.sumOrderAmountByVendorId(vendorId);
        BigDecimal totalCommission = commissionRecordRepository.sumCommissionAmountByVendorId(vendorId);
        long totalOrders = commissionRecordRepository.countByVendorId(vendorId);

        BigDecimal totalNetEarnings = totalSales != null && totalCommission != null
            ? totalSales.subtract(totalCommission)
            : BigDecimal.ZERO;

        return VendorEarningsResponse.of(
            vendorId,
            totalSales != null ? totalSales : BigDecimal.ZERO,
            totalCommission != null ? totalCommission : BigDecimal.ZERO,
            totalNetEarnings,
            (int) totalOrders
        );
    }

    @Transactional(readOnly = true)
    public VendorEarningsResponse execute(Long vendorId, Instant start, Instant end) {
        BigDecimal totalSales = commissionRecordRepository.sumOrderAmountByVendorIdAndCreatedAtBetween(vendorId, start, end);
        BigDecimal totalCommission = commissionRecordRepository.sumCommissionAmountByVendorIdAndCreatedAtBetween(vendorId, start, end);
        List<CommissionRecord> records = commissionRecordRepository.findByVendorIdAndCreatedAtBetween(vendorId, start, end);

        BigDecimal totalNetEarnings = totalSales != null && totalCommission != null
            ? totalSales.subtract(totalCommission)
            : BigDecimal.ZERO;

        return VendorEarningsResponse.builder()
            .vendorId(vendorId)
            .totalSales(totalSales != null ? totalSales : BigDecimal.ZERO)
            .totalCommission(totalCommission != null ? totalCommission : BigDecimal.ZERO)
            .totalNetEarnings(totalNetEarnings)
            .averageOrderValue(!records.isEmpty() ? totalSales.divide(BigDecimal.valueOf(records.size()), 2, java.math.RoundingMode.HALF_UP) : BigDecimal.ZERO)
            .totalOrders(records.size())
            .periodStart(start)
            .periodEnd(end)
            .build();
    }

    @Transactional(readOnly = true)
    public List<CommissionRecordResponse> getVendorRecords(Long vendorId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<CommissionRecord> records = commissionRecordRepository.findByVendorId(vendorId, pageRequest);

        return records.getContent().stream()
            .map(CommissionRecordResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CommissionRecordResponse> getVendorUnsettledRecords(Long vendorId) {
        List<CommissionRecord> records = commissionRecordRepository.findByVendorIdAndIsSettled(vendorId, false);

        return records.stream()
            .map(CommissionRecordResponse::from)
            .collect(Collectors.toList());
    }
}