package com.marketplace.commission.domain.event;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CommissionCalculatedEvent extends DomainEvent {

    private final Long commissionRecordId;
    private final Long orderId;
    private final Long vendorId;
    private final BigDecimal orderAmount;
    private final BigDecimal commissionAmount;

    public CommissionCalculatedEvent(Object source, Long commissionRecordId, Long orderId,
                                     Long vendorId, BigDecimal orderAmount, BigDecimal commissionAmount) {
        super(source);
        this.commissionRecordId = commissionRecordId;
        this.orderId = orderId;
        this.vendorId = vendorId;
        this.orderAmount = orderAmount;
        this.commissionAmount = commissionAmount;
    }
}