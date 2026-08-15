package com.marketplace.commission.domain.exception;

public class SettlementNotFoundException extends RuntimeException {

    public SettlementNotFoundException(Long settlementId) {
        super("Settlement not found with ID: " + settlementId);
    }

    public SettlementNotFoundException(String message) {
        super(message);
    }
}