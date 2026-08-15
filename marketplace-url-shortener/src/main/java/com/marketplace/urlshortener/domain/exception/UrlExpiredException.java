package com.marketplace.urlshortener.domain.exception;

public class UrlExpiredException extends RuntimeException {

    public UrlExpiredException(String shortCode) {
        super("URL has expired: " + shortCode);
    }
}