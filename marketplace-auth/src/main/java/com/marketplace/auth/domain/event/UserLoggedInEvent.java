package com.marketplace.auth.domain.event;

import lombok.Getter;

@Getter
public class UserLoggedInEvent extends DomainEvent {

    private final Long userId;
    private final String ipAddress;
    private final String userAgent;

    public UserLoggedInEvent(Long userId, String ipAddress, String userAgent) {
        super();
        this.userId = userId;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
    }
}