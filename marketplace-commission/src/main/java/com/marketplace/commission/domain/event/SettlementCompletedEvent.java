package com.marketplace.commission.domain.event;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class SettlementCompletedEvent extends DomainEvent {

    private final Long settlementId;
    private final Long vendorId;
    private final BigDecimal totalAmount;
    private final BigDecimal commissionAmount;
    private final BigDecimal netPayout;

    public SettlementCompletedEvent(Object source, Long settlementId, Long vendorId,
                                    BigDecimal totalAmount, BigDecimal commissionAmount, BigDecimal netPayout) {
        super(source);
        this.settlementId = settlementId;
        this.vendorId = vendorId;
        this.totalAmount = totalAmount;
        this.commissionAmount = commissionAmount;
        this.netPayout = netPayout;
    }
}