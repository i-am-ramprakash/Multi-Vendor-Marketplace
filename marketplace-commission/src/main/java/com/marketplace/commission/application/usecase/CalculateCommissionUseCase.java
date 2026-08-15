package com.marketplace.commission.application.usecase;

import com.marketplace.commission.application.dto.CommissionRecordResponse;
import com.marketplace.commission.domain.entity.CommissionRecord;
import com.marketplace.commission.domain.entity.CommissionRule;
import com.marketplace.commission.domain.event.CommissionCalculatedEvent;
import com.marketplace.commission.domain.repository.CommissionRecordRepository;
import com.marketplace.commission.domain.service.CommissionCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class CalculateCommissionUseCase {

    private final CommissionRecordRepository commissionRecordRepository;
    private final CommissionCalculationService commissionCalculationService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public CommissionRecordResponse execute(Long orderId, Long orderItemId, Long vendorId,
                                           Long categoryId, BigDecimal orderAmount, String currency) {
        // Find applicable commission rule
        CommissionRule rule = commissionCalculationService.findApplicableRule(vendorId, categoryId);

        // Calculate commission
        BigDecimal commissionRate = rule.getRate();
        BigDecimal commissionAmount = rule.calculateCommission(orderAmount);

        // Create commission record
        CommissionRecord record = new CommissionRecord(
            orderId,
            orderItemId,
            vendorId,
            rule.getId(),
            orderAmount,
            commissionRate,
            currency
        );

        record.calculateAmounts();

        // Save record
        CommissionRecord savedRecord = commissionRecordRepository.save(record);

        // Publish event
        eventPublisher.publishEvent(new CommissionCalculatedEvent(
            this,
            savedRecord.getId(),
            orderId,
            vendorId,
            orderAmount,
            commissionAmount
        ));

        return CommissionRecordResponse.from(savedRecord);
    }
}