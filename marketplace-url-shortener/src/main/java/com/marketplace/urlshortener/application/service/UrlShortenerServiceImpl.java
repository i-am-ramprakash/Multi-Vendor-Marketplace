package com.marketplace.urlshortener.application.service;

import com.marketplace.urlshortener.application.dto.*;
import com.marketplace.urlshortener.application.usecase.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UrlShortenerServiceImpl implements UrlShortenerService {

    private final CreateShortUrlUseCase createShortUrlUseCase;
    private final ResolveUrlUseCase resolveUrlUseCase;
    private final GetShortUrlUseCase getShortUrlUseCase;
    private final GetUserShortUrlsUseCase getUserShortUrlsUseCase;
    private final GetUrlAnalyticsUseCase getUrlAnalyticsUseCase;
    private final GetClickAnalyticsUseCase getClickAnalyticsUseCase;
    private final DeactivateShortUrlUseCase deactivateShortUrlUseCase;
    private final GetUrlStatsUseCase getUrlStatsUseCase;

    @Override
    @Transactional
    public ShortUrlResponse createShortUrl(CreateShortUrlRequest request) {
        return createShortUrlUseCase.execute(request);
    }

    @Override
    @Transactional
    public String resolveUrl(String shortCode, String ipAddress, String userAgent, String referer, Long userId) {
        return resolveUrlUseCase.execute(shortCode, ipAddress, userAgent, referer, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public ShortUrlResponse getShortUrl(Long id) {
        return getShortUrlUseCase.execute(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ShortUrlResponse getShortUrlByCode(String shortCode) {
        return getShortUrlUseCase.executeByShortCode(shortCode);
    }

    @Override
    @Transactional(readOnly = true)
    public ShortUrlListResponse getUserShortUrls(Long userId, int page, int size) {
        return getUserShortUrlsUseCase.execute(userId, page, size);
    }

    @Override
    @Transactional(readOnly = true)
    public UrlAnalyticsResponse getUrlAnalytics(Long shortUrlId) {
        return getUrlAnalyticsUseCase.execute(shortUrlId);
    }

    @Override
    @Transactional(readOnly = true)
    public UrlAnalyticsResponse getUrlAnalyticsByCode(String shortCode) {
        return getUrlAnalyticsUseCase.executeByShortCode(shortCode);
    }

    @Override
    @Transactional(readOnly = true)
    public ClickAnalyticsResponse getClickAnalytics(String shortCode, int days) {
        return getClickAnalyticsUseCase.execute(shortCode, days);
    }

    @Override
    @Transactional
    public ShortUrlResponse deactivateShortUrl(Long id, Long performedBy) {
        return deactivateShortUrlUseCase.execute(id, performedBy);
    }

    @Override
    @Transactional
    public ShortUrlResponse deactivateShortUrlByCode(String shortCode, Long performedBy) {
        return deactivateShortUrlUseCase.executeByShortCode(shortCode, performedBy);
    }

    @Override
    @Transactional(readOnly = true)
    public UrlStatsResponse getUrlStats() {
        return getUrlStatsUseCase.execute();
    }
}