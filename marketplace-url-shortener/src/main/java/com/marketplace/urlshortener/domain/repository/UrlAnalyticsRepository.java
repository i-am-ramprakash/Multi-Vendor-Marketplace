package com.marketplace.urlshortener.domain.repository;

import com.marketplace.urlshortener.domain.entity.UrlAnalytics;

import java.util.Optional;

public interface UrlAnalyticsRepository {

    UrlAnalytics save(UrlAnalytics urlAnalytics);

    Optional<UrlAnalytics> findByShortUrlId(Long shortUrlId);

    Optional<UrlAnalytics> findByShortCode(String shortCode);
}