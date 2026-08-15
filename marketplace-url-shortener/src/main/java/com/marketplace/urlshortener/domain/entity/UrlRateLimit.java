package com.marketplace.urlshortener.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UrlRateLimit {

    private Long id;
    private String identifier;
    private String type;
    private int requestCount;
    private int maxRequests;
    private Instant windowStart;
    private Instant windowEnd;
    private boolean isBlocked;
    private Instant blockedUntil;
    private Instant createdAt;
    private Instant updatedAt;

    public UrlRateLimit(String identifier, String type, int maxRequests, int windowMinutes) {
        this.identifier = identifier;
        this.type = type;
        this.maxRequests = maxRequests;
        this.requestCount = 0;
        this.windowStart = Instant.now();
        this.windowEnd = Instant.now().plusSeconds(windowMinutes * 60L);
        this.isBlocked = false;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public boolean canMakeRequest() {
        if (isBlocked && blockedUntil != null && Instant.now().isBefore(blockedUntil)) {
            return false;
        }
        if (Instant.now().isAfter(windowEnd)) {
            resetWindow();
        }
        return requestCount < maxRequests;
    }

    public void incrementRequestCount() {
        this.requestCount++;
        this.updatedAt = Instant.now();
    }

    public void block(int blockMinutes) {
        this.isBlocked = true;
        this.blockedUntil = Instant.now().plusSeconds(blockMinutes * 60L);
        this.updatedAt = Instant.now();
    }

    public void resetWindow() {
        this.requestCount = 0;
        this.windowStart = Instant.now();
        this.windowEnd = Instant.now().plusSeconds(60 * 60L); // 1 hour default
        this.isBlocked = false;
        this.blockedUntil = null;
        this.updatedAt = Instant.now();
    }
}