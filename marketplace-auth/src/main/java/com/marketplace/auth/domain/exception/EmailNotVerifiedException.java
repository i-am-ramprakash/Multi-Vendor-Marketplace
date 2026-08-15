package com.marketplace.auth.domain.exception;

public class EmailNotVerifiedException extends DomainException {

    public EmailNotVerifiedException() {
        super("EMAIL_NOT_VERIFIED", "Email address not verified. Please verify your email before logging in.");
    }
}