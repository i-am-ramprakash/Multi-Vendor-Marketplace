package com.marketplace.urlshortener.infrastructure.persistence.mapper;

import com.marketplace.urlshortener.domain.entity.UrlRateLimit;
import com.marketplace.urlshortener.infrastructure.persistence.entity.UrlRateLimitJpaEntity;

import java.lang.reflect.Field;

public final class UrlRateLimitPersistenceMapper {

    private UrlRateLimitPersistenceMapper() {}

    public static UrlRateLimitJpaEntity toJpaEntity(UrlRateLimit domain) {
        if (domain == null) return null;

        UrlRateLimitJpaEntity jpa = new UrlRateLimitJpaEntity();
        jpa.setId(domain.getId());
        jpa.setIdentifier(domain.getIdentifier());
        jpa.setType(domain.getType());
        jpa.setRequestCount(domain.getRequestCount());
        jpa.setMaxRequests(domain.getMaxRequests());
        jpa.setWindowStart(domain.getWindowStart());
        jpa.setWindowEnd(domain.getWindowEnd());
        jpa.setBlocked(domain.isBlocked());
        jpa.setBlockedUntil(domain.getBlockedUntil());
        jpa.setCreatedAt(domain.getCreatedAt());
        jpa.setUpdatedAt(domain.getUpdatedAt());

        return jpa;
    }

    public static UrlRateLimit toDomain(UrlRateLimitJpaEntity jpa) {
        if (jpa == null) return null;

        UrlRateLimit rateLimit = new UrlRateLimit(
            jpa.getIdentifier(),
            jpa.getType(),
            jpa.getMaxRequests(),
            60 // Default window
        );
        setId(rateLimit, jpa.getId());
        rateLimit.setRequestCount(jpa.getRequestCount());
        rateLimit.setWindowStart(jpa.getWindowStart());
        rateLimit.setWindowEnd(jpa.getWindowEnd());
        rateLimit.setBlocked(jpa.isBlocked());
        rateLimit.setBlockedUntil(jpa.getBlockedUntil());
        rateLimit.setCreatedAt(jpa.getCreatedAt());
        rateLimit.setUpdatedAt(jpa.getUpdatedAt());

        return rateLimit;
    }

    private static void setId(UrlRateLimit rateLimit, Long id) {
        try {
            Field field = UrlRateLimit.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(rateLimit, id);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot set URL rate limit ID", e);
        }
    }
}