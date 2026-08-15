package com.marketplace.commission.application.usecase;

import com.marketplace.commission.application.dto.SettlementResponse;
import com.marketplace.commission.domain.entity.CommissionRecord;
import com.marketplace.commission.domain.entity.CommissionAuditLog;
import com.marketplace.commission.domain.entity.Settlement;
import com.marketplace.commission.domain.repository.CommissionAuditLogRepository;
import com.marketplace.commission.domain.repository.CommissionRecordRepository;
import com.marketplace.commission.domain.repository.SettlementRepository;
import com.marketplace.commission.domain.valueobject.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CreateSettlementUseCase {

    private final SettlementRepository settlementRepository;
    private final CommissionRecordRepository commissionRecordRepository;
    private final CommissionAuditLogRepository auditLogRepository;

    @Transactional
    public SettlementResponse execute(Long vendorId, Instant periodStart, Instant periodEnd) {
        // Get unsettled records for the vendor
        List<CommissionRecord> unsettledRecords = commissionRecordRepository.findByVendorIdAndIsSettled(vendorId, false);

        if (unsettledRecords.isEmpty()) {
            throw new IllegalStateException("No unsettled records found for vendor: " + vendorId);
        }

        // Create settlement
        Settlement settlement = new Settlement(vendorId, "USD", periodStart, periodEnd);

        // Add commission records to settlement
        for (CommissionRecord record : unsettledRecords) {
            settlement.addCommissionRecord(
                record.getId(),
                record.getOrderAmount(),
                record.getCommissionAmount()
            );
        }

        // Save settlement
        Settlement savedSettlement = settlementRepository.save(settlement);

        // Mark records as settled
        for (CommissionRecord record : unsettledRecords) {
            record.markAsSettled(savedSettlement.getId());
            commissionRecordRepository.save(record);
        }

        // Create audit log
        CommissionAuditLog auditLog = new CommissionAuditLog("SETTLEMENT_CREATED", null,
            "Settlement created for vendor " + vendorId + " with " + unsettledRecords.size() + " records");
        auditLog.setVendorId(vendorId);
        auditLog.setSettlementId(savedSettlement.getId());
        auditLogRepository.save(auditLog);

        return SettlementResponse.from(savedSettlement);
    }
}