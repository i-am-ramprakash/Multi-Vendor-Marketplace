package com.marketplace.auth.domain.event;

import com.marketplace.auth.domain.valueobject.Email;
import lombok.Getter;


@Getter
public class UserRegisteredEvent extends DomainEvent {

    private final Long userId;
    private final Email email;
    private final String firstName;
    private final String lastName;
    private final String role;

    public UserRegisteredEvent(Long userId, Email email, String firstName, String lastName, String role) {
        super();
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }
}