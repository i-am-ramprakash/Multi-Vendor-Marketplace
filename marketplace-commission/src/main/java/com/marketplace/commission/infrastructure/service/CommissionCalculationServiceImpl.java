package com.marketplace.commission.infrastructure.service;

import com.marketplace.commission.domain.entity.CommissionRule;
import com.marketplace.commission.domain.exception.CommissionRuleNotFoundException;
import com.marketplace.commission.domain.repository.CommissionRuleRepository;
import com.marketplace.commission.domain.service.CommissionCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommissionCalculationServiceImpl implements CommissionCalculationService {

    private final CommissionRuleRepository commissionRuleRepository;

    @Override
    public BigDecimal calculateCommission(Long vendorId, Long categoryId, BigDecimal orderAmount) {
        CommissionRule rule = findApplicableRule(vendorId, categoryId);
        return rule.calculateCommission(orderAmount);
    }

    @Override
    public CommissionRule findApplicableRule(Long vendorId, Long categoryId) {
        List<CommissionRule> activeRules = commissionRuleRepository.findByIsActiveTrue();

        // Find vendor-specific rule first
        for (CommissionRule rule : activeRules) {
            if (rule.getVendorId() != null && rule.getVendorId().equals(vendorId) &&
                rule.getCategoryId() != null && rule.getCategoryId().equals(categoryId) &&
                rule.isCurrentlyEffective()) {
                return rule;
            }
        }

        // Find vendor-specific rule without category
        for (CommissionRule rule : activeRules) {
            if (rule.getVendorId() != null && rule.getVendorId().equals(vendorId) &&
                rule.getCategoryId() == null &&
                rule.isCurrentlyEffective()) {
                return rule;
            }
        }

        // Find category-specific rule
        for (CommissionRule rule : activeRules) {
            if (rule.getVendorId() == null && rule.getCategoryId() != null &&
                rule.getCategoryId().equals(categoryId) &&
                rule.isCurrentlyEffective()) {
                return rule;
            }
        }

        // Find default rule
        return commissionRuleRepository.findDefaultRule()
            .orElseThrow(() -> new CommissionRuleNotFoundException("No applicable commission rule found"));
    }
}