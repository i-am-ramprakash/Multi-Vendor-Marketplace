package com.marketplace.commission.application.usecase;

import com.marketplace.commission.application.dto.SettlementResponse;
import com.marketplace.commission.domain.entity.CommissionAuditLog;
import com.marketplace.commission.domain.entity.Settlement;
import com.marketplace.commission.domain.event.SettlementCompletedEvent;
import com.marketplace.commission.domain.exception.SettlementNotFoundException;
import com.marketplace.commission.domain.repository.CommissionAuditLogRepository;
import com.marketplace.commission.domain.repository.SettlementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CompleteSettlementUseCase {

    private final SettlementRepository settlementRepository;
    private final CommissionAuditLogRepository auditLogRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public SettlementResponse execute(Long settlementId, String paymentReference, Long performedBy) {
        Settlement settlement = settlementRepository.findById(settlementId)
            .orElseThrow(() -> new SettlementNotFoundException(settlementId));

        // Complete settlement
        settlement.complete(paymentReference);

        // Save settlement
        Settlement savedSettlement = settlementRepository.save(settlement);

        // Create audit log
        CommissionAuditLog auditLog = new CommissionAuditLog("SETTLEMENT_COMPLETED", performedBy,
            "Settlement " + settlement.getSettlementNumber() + " completed. Payment reference: " + paymentReference);
        auditLog.setVendorId(settlement.getVendorId());
        auditLog.setSettlementId(settlementId);
        auditLogRepository.save(auditLog);

        // Publish event
        eventPublisher.publishEvent(new SettlementCompletedEvent(
            this,
            settlementId,
            settlement.getVendorId(),
            settlement.getTotalAmount().getAmount(),
            settlement.getCommissionAmount().getAmount(),
            settlement.getNetPayout().getAmount()
        ));

        return SettlementResponse.from(savedSettlement);
    }
}