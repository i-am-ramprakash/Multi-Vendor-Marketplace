package com.marketplace.auth.domain.exception;

public class UserNotActiveException extends DomainException {

    public UserNotActiveException(String status) {
        super("USER_NOT_ACTIVE", "User account is not active: " + status);
    }
}