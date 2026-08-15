package com.marketplace.commission.application.usecase;

import com.marketplace.commission.application.dto.CreateCommissionRuleRequest;
import com.marketplace.commission.application.dto.CommissionRuleResponse;
import com.marketplace.commission.domain.entity.CommissionRule;
import com.marketplace.commission.domain.exception.CommissionRuleNotFoundException;
import com.marketplace.commission.domain.repository.CommissionRuleRepository;
import com.marketplace.commission.domain.valueobject.CommissionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreateCommissionRuleUseCase {

    private final CommissionRuleRepository commissionRuleRepository;

    @Transactional
    public CommissionRuleResponse execute(CreateCommissionRuleRequest request) {
        CommissionType type = CommissionType.valueOf(request.getType());

        CommissionRule rule = new CommissionRule(
            request.getName(),
            request.getDescription(),
            type,
            request.getRate()
        );

        if (request.getFixedAmount() != null) {
            rule.setFixedAmount(request.getFixedAmount());
        }
        if (request.getMinOrderAmount() != null) {
            rule.setMinOrderAmount(request.getMinOrderAmount());
        }
        if (request.getMaxCommissionAmount() != null) {
            rule.setMaxCommissionAmount(request.getMaxCommissionAmount());
        }
        if (request.getCategoryId() != null) {
            rule.setCategoryId(request.getCategoryId());
        }
        if (request.getVendorId() != null) {
            rule.setVendorId(request.getVendorId());
        }
        rule.setDefault(request.isDefault());
        if (request.getEffectiveFrom() != null) {
            rule.setEffectiveFrom(request.getEffectiveFrom());
        }
        if (request.getEffectiveTo() != null) {
            rule.setEffectiveTo(request.getEffectiveTo());
        }

        CommissionRule savedRule = commissionRuleRepository.save(rule);
        return CommissionRuleResponse.from(savedRule);
    }
}