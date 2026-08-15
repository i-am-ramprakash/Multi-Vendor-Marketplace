package com.marketplace.auth.domain.exception;

public class TokenException extends DomainException {

    public TokenException(String code, String message) {
        super(code, message);
    }

    public static TokenException expired() {
        return new TokenException("TOKEN_EXPIRED", "Token has expired");
    }

    public static TokenException invalid() {
        return new TokenException("TOKEN_INVALID", "Token is invalid");
    }

    public static TokenException revoked() {
        return new TokenException("TOKEN_REVOKED", "Token has been revoked");
    }

    public static TokenException malformed() {
        return new TokenException("TOKEN_MALFORMED", "Token is malformed");
    }
}