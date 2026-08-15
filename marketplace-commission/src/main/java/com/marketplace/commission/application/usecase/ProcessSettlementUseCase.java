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
public class ProcessSettlementUseCase {

    private final SettlementRepository settlementRepository;
    private final CommissionAuditLogRepository auditLogRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public SettlementResponse execute(Long settlementId, Long performedBy) {
        Settlement settlement = settlementRepository.findById(settlementId)
            .orElseThrow(() -> new SettlementNotFoundException(settlementId));

        if (!settlement.canBeProcessed()) {
            throw new IllegalStateException("Settlement cannot be processed");
        }

        // Process settlement
        settlement.process();

        // Save settlement
        Settlement savedSettlement = settlementRepository.save(settlement);

        // Create audit log
        CommissionAuditLog auditLog = new CommissionAuditLog("SETTLEMENT_PROCESSED", performedBy,
            "Settlement " + settlement.getSettlementNumber() + " processing started");
        auditLog.setVendorId(settlement.getVendorId());
        auditLog.setSettlementId(settlementId);
        auditLogRepository.save(auditLog);

        return SettlementResponse.from(savedSettlement);
    }
}