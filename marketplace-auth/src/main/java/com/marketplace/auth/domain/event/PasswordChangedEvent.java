package com.marketplace.auth.domain.event;

import lombok.Getter;

@Getter
public class PasswordChangedEvent extends DomainEvent {

    private final Long userId;
    private final boolean selfInitiated;

    public PasswordChangedEvent(Long userId, boolean selfInitiated) {
        super();
        this.userId = userId;
        this.selfInitiated = selfInitiated;
    }
}