package com.marketplace.urlshortener.application.usecase;

import com.marketplace.urlshortener.application.dto.CreateShortUrlRequest;
import com.marketplace.urlshortener.application.dto.ShortUrlResponse;
import com.marketplace.urlshortener.domain.entity.ShortUrl;
import com.marketplace.urlshortener.domain.entity.UrlAnalytics;
import com.marketplace.urlshortener.domain.event.UrlCreatedEvent;
import com.marketplace.urlshortener.domain.exception.InvalidUrlException;
import com.marketplace.urlshortener.domain.exception.ShortCodeAlreadyExistsException;
import com.marketplace.urlshortener.domain.repository.ShortUrlRepository;
import com.marketplace.urlshortener.domain.repository.UrlAnalyticsRepository;
import com.marketplace.urlshortener.domain.valueobject.ExpirationType;
import com.marketplace.urlshortener.domain.valueobject.UrlType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CreateShortUrlUseCase {

    private final ShortUrlRepository shortUrlRepository;
    private final UrlAnalyticsRepository analyticsRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Value("${app.url.shortener.base-url:http://localhost:8088/s}")
    private String baseUrl;

    @Transactional
    public ShortUrlResponse execute(CreateShortUrlRequest request) {
        // Validate URL
        validateUrl(request.getOriginalUrl());

        // Generate or validate short code
        String shortCode;
        if (request.getCustomAlias() != null && !request.getCustomAlias().isBlank()) {
            if (shortUrlRepository.findByShortCode(request.getCustomAlias()).isPresent()) {
                throw new ShortCodeAlreadyExistsException(request.getCustomAlias());
            }
            shortCode = request.getCustomAlias();
        } else {
            shortCode = generateShortCode();
        }

        // Create short URL entity
        UrlType type = UrlType.valueOf(request.getType());
        ShortUrl shortUrl = new ShortUrl(shortCode, request.getOriginalUrl(), type, request.getCreatedBy());
        shortUrl.setTitle(request.getTitle());
        shortUrl.setDescription(request.getDescription());
        shortUrl.setReferenceId(request.getReferenceId());
        shortUrl.setReferenceType(request.getReferenceType());
        shortUrl.setCustomAlias(request.getCustomAlias() != null && !request.getCustomAlias().isBlank());
        shortUrl.setTags(request.getTags());
        shortUrl.setMetadata(request.getMetadata());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            shortUrl.setPassword(request.getPassword());
            shortUrl.setRequiresPassword(true);
        }

        // Set expiration
        if (request.getExpirationType() != null) {
            ExpirationType expirationType = ExpirationType.valueOf(request.getExpirationType());
            Instant expiresAt = calculateExpiration(expirationType, request.getExpirationMinutes());
            shortUrl.setExpiration(expirationType, expiresAt);
        }

        // Save short URL
        ShortUrl savedShortUrl = shortUrlRepository.save(shortUrl);

        // Create analytics entity
        UrlAnalytics analytics = new UrlAnalytics(savedShortUrl.getId(), savedShortUrl.getShortCode());
        analyticsRepository.save(analytics);

        // Publish event
        eventPublisher.publishEvent(new UrlCreatedEvent(
            this,
            savedShortUrl.getId(),
            savedShortUrl.getShortCode(),
            savedShortUrl.getOriginalUrl(),
            type.name(),
            request.getCreatedBy()
        ));

        return ShortUrlResponse.from(savedShortUrl, baseUrl);
    }

    private void validateUrl(String url) {
        try {
            new URL(url).toURI();
        } catch (Exception e) {
            throw new InvalidUrlException("Invalid URL format: " + url);
        }
    }

    private String generateShortCode() {
        String uuid = UUID.randomUUID().toString();
        return Base64.getUrlEncoder().withoutPadding().encodeToString(uuid.getBytes())
            .substring(0, 7);
    }

    private Instant calculateExpiration(ExpirationType type, Long customMinutes) {
        Instant now = Instant.now();
        return switch (type) {
            case NONE -> null;
            case MINUTES_5 -> now.plus(5, ChronoUnit.MINUTES);
            case MINUTES_30 -> now.plus(30, ChronoUnit.MINUTES);
            case HOURS_1 -> now.plus(1, ChronoUnit.HOURS);
            case HOURS_24 -> now.plus(24, ChronoUnit.HOURS);
            case DAYS_7 -> now.plus(7, ChronoUnit.DAYS);
            case DAYS_30 -> now.plus(30, ChronoUnit.DAYS);
            case DAYS_90 -> now.plus(90, ChronoUnit.DAYS);
            case CUSTOM -> customMinutes != null ? now.plus(customMinutes, ChronoUnit.MINUTES) : null;
        };
    }
}