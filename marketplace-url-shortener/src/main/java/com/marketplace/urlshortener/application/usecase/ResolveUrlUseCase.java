package com.marketplace.urlshortener.application.usecase;

import com.marketplace.urlshortener.domain.entity.ShortUrl;
import com.marketplace.urlshortener.domain.entity.UrlClick;
import com.marketplace.urlshortener.domain.event.UrlClickedEvent;
import com.marketplace.urlshortener.domain.exception.UrlExpiredException;
import com.marketplace.urlshortener.domain.exception.UrlNotFoundException;
import com.marketplace.urlshortener.domain.repository.ShortUrlRepository;
import com.marketplace.urlshortener.domain.repository.UrlAnalyticsRepository;
import com.marketplace.urlshortener.domain.repository.UrlClickRepository;
import com.marketplace.urlshortener.domain.service.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class ResolveUrlUseCase {

    private final ShortUrlRepository shortUrlRepository;
    private final UrlClickRepository clickRepository;
    private final UrlAnalyticsRepository analyticsRepository;
    private final CacheService cacheService;
    private final ApplicationEventPublisher eventPublisher;

    @Value("${app.url.shortener.cache.ttl-seconds:3600}")
    private int cacheTtlSeconds;

    @Transactional
    public String execute(String shortCode, String ipAddress, String userAgent, String referer, Long userId) {
        // Try to get from cache first
        String cachedUrl = cacheService.get("url:" + shortCode);
        if (cachedUrl != null) {
            // Track click asynchronously
            trackClick(shortCode, ipAddress, userAgent, referer, userId);
            return cachedUrl;
        }

        // Find short URL
        ShortUrl shortUrl = shortUrlRepository.findByShortCode(shortCode)
            .orElseThrow(() -> new UrlNotFoundException(shortCode));

        // Check if URL can be accessed
        if (!shortUrl.canBeAccessed()) {
            if (shortUrl.isExpired()) {
                throw new UrlExpiredException(shortCode);
            }
            throw new UrlNotFoundException("URL is not active: " + shortCode);
        }

        // Cache the URL
        cacheService.put("url:" + shortCode, shortUrl.getOriginalUrl(), cacheTtlSeconds);

        // Track click
        trackClick(shortCode, ipAddress, userAgent, referer, userId);

        return shortUrl.getOriginalUrl();
    }

    private void trackClick(String shortCode, String ipAddress, String userAgent, String referer, Long userId) {
        try {
            // Find short URL
            ShortUrl shortUrl = shortUrlRepository.findByShortCode(shortCode).orElse(null);
            if (shortUrl == null) return;

            // Check if this is a unique click (same IP in last 24 hours)
            boolean isUnique = !isDuplicateClick(shortCode, ipAddress);

            // Create click record
            UrlClick click = new UrlClick(
                shortUrl.getId(),
                shortCode,
                ipAddress,
                userAgent,
                referer,
                isUnique,
                userId
            );

            // Parse device info from user agent
            parseDeviceInfo(click, userAgent);

            // Save click
            clickRepository.save(click);

            // Update short URL click count
            if (isUnique) {
                shortUrl.recordUniqueClick();
            } else {
                shortUrl.recordClick();
            }
            shortUrlRepository.save(shortUrl);

            // Update analytics
            updateAnalytics(shortCode, isUnique, click);

            // Publish event
            eventPublisher.publishEvent(new UrlClickedEvent(
                this,
                shortUrl.getId(),
                shortCode,
                ipAddress,
                referer,
                isUnique
            ));

        } catch (Exception e) {
            // Log error but don't fail the redirect
            e.printStackTrace();
        }
    }

    private boolean isDuplicateClick(String shortCode, String ipAddress) {
        // Check cache for recent clicks from this IP
        String cacheKey = "click:" + shortCode + ":" + ipAddress;
        String lastClick = cacheService.get(cacheKey);
        if (lastClick != null) {
            return true;
        }
        // Set cache with 24 hour TTL
        cacheService.put(cacheKey, "1", 86400);
        return false;
    }

    private void parseDeviceInfo(UrlClick click, String userAgent) {
        if (userAgent == null) return;

        // Simple device detection
        if (userAgent.contains("Mobile") || userAgent.contains("Android")) {
            click.setDeviceInfo("Mobile", extractBrowser(userAgent), extractOs(userAgent));
        } else if (userAgent.contains("Tablet") || userAgent.contains("iPad")) {
            click.setDeviceInfo("Tablet", extractBrowser(userAgent), extractOs(userAgent));
        } else {
            click.setDeviceInfo("Desktop", extractBrowser(userAgent), extractOs(userAgent));
        }
    }

    private String extractBrowser(String userAgent) {
        if (userAgent.contains("Chrome")) return "Chrome";
        if (userAgent.contains("Firefox")) return "Firefox";
        if (userAgent.contains("Safari")) return "Safari";
        if (userAgent.contains("Edge")) return "Edge";
        if (userAgent.contains("Opera")) return "Opera";
        return "Other";
    }

    private String extractOs(String userAgent) {
        if (userAgent.contains("Windows")) return "Windows";
        if (userAgent.contains("Mac OS")) return "macOS";
        if (userAgent.contains("Linux")) return "Linux";
        if (userAgent.contains("Android")) return "Android";
        if (userAgent.contains("iOS")) return "iOS";
        return "Other";
    }

    private void updateAnalytics(String shortCode, boolean isUnique, UrlClick click) {
        analyticsRepository.findByShortCode(shortCode).ifPresent(analytics -> {
            analytics.incrementClicks(isUnique);
            if (click.getCountry() != null) {
                analytics.updateTopMetrics(
                    click.getCountry(),
                    click.getCity(),
                    click.getDevice(),
                    click.getBrowser(),
                    click.getReferer()
                );
            }
            analyticsRepository.save(analytics);
        });
    }
}