package com.marketplace.auth.domain.event;

import lombok.Getter;

@Getter
public class EmailVerifiedEvent extends DomainEvent {

    private final Long userId;
    private final String email;

    public EmailVerifiedEvent(Long userId, String email) {
        super();
        this.userId = userId;
        this.email = email;
    }
}