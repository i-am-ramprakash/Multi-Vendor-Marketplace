package com.marketplace.commission.domain.exception;

public class InvalidSettlementStateException extends RuntimeException {

    public InvalidSettlementStateException(String currentStatus, String attemptedAction) {
        super(String.format("Cannot %s settlement in %s state", attemptedAction, currentStatus));
    }
}