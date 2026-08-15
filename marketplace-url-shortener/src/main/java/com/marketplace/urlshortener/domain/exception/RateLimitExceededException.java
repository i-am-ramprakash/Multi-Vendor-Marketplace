package com.marketplace.urlshortener.domain.exception;

public class RateLimitExceededException extends RuntimeException {

    public RateLimitExceededException(String message) {
        super(message);
    }

    public RateLimitExceededException(String identifier, int maxRequests) {
        super("Rate limit exceeded for " + identifier + ". Max requests: " + maxRequests);
    }
}