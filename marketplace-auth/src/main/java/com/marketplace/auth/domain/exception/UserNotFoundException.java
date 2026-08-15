package com.marketplace.auth.domain.exception;

public class UserNotFoundException extends DomainException {

    public UserNotFoundException(String identifier) {
        super("USER_NOT_FOUND", "User not found: " + identifier);
    }

    public UserNotFoundException(Long id) {
        super("USER_NOT_FOUND", "User not found with id: " + id);
    }
}