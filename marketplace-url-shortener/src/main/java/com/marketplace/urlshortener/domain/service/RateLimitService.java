package com.marketplace.urlshortener.domain.service;

public interface RateLimitService {

    boolean isAllowed(String identifier, String type, int maxRequests, int windowMinutes);

    void recordRequest(String identifier, String type);

    void block(String identifier, String type, int blockMinutes);

    int getRequestCount(String identifier, String type);
}