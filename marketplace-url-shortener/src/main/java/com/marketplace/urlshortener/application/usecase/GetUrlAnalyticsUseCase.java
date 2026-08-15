package com.marketplace.urlshortener.application.usecase;

import com.marketplace.urlshortener.application.dto.UrlAnalyticsResponse;
import com.marketplace.urlshortener.domain.entity.UrlAnalytics;
import com.marketplace.urlshortener.domain.exception.UrlNotFoundException;
import com.marketplace.urlshortener.domain.repository.UrlAnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GetUrlAnalyticsUseCase {

    private final UrlAnalyticsRepository analyticsRepository;

    @Transactional(readOnly = true)
    public UrlAnalyticsResponse execute(Long shortUrlId) {
        UrlAnalytics analytics = analyticsRepository.findByShortUrlId(shortUrlId)
            .orElseThrow(() -> new UrlNotFoundException(shortUrlId));
        return UrlAnalyticsResponse.from(analytics);
    }

    @Transactional(readOnly = true)
    public UrlAnalyticsResponse executeByShortCode(String shortCode) {
        UrlAnalytics analytics = analyticsRepository.findByShortCode(shortCode)
            .orElseThrow(() -> new UrlNotFoundException(shortCode));
        return UrlAnalyticsResponse.from(analytics);
    }
}