package com.marketplace.urlshortener.domain.repository;

import com.marketplace.urlshortener.domain.entity.UrlRateLimit;

import java.util.Optional;

public interface UrlRateLimitRepository {

    UrlRateLimit save(UrlRateLimit urlRateLimit);

    Optional<UrlRateLimit> findByIdentifierAndType(String identifier, String type);
}