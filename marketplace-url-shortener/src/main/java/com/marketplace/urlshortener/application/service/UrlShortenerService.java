package com.marketplace.urlshortener.application.service;

import com.marketplace.urlshortener.application.dto.*;

public interface UrlShortenerService {

    ShortUrlResponse createShortUrl(CreateShortUrlRequest request);

    String resolveUrl(String shortCode, String ipAddress, String userAgent, String referer, Long userId);

    ShortUrlResponse getShortUrl(Long id);

    ShortUrlResponse getShortUrlByCode(String shortCode);

    ShortUrlListResponse getUserShortUrls(Long userId, int page, int size);

    UrlAnalyticsResponse getUrlAnalytics(Long shortUrlId);

    UrlAnalyticsResponse getUrlAnalyticsByCode(String shortCode);

    ClickAnalyticsResponse getClickAnalytics(String shortCode, int days);

    ShortUrlResponse deactivateShortUrl(Long id, Long performedBy);

    ShortUrlResponse deactivateShortUrlByCode(String shortCode, Long performedBy);

    UrlStatsResponse getUrlStats();
}