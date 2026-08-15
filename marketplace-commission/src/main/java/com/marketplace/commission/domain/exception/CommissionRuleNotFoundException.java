package com.marketplace.commission.domain.exception;

public class CommissionRuleNotFoundException extends RuntimeException {

    public CommissionRuleNotFoundException(Long ruleId) {
        super("Commission rule not found with ID: " + ruleId);
    }

    public CommissionRuleNotFoundException(String message) {
        super(message);
    }
}