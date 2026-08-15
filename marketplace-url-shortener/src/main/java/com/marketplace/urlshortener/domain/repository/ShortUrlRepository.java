package com.marketplace.urlshortener.domain.repository;

import com.marketplace.urlshortener.domain.entity.ShortUrl;
import com.marketplace.urlshortener.domain.valueobject.UrlStatus;
import com.marketplace.urlshortener.domain.valueobject.UrlType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ShortUrlRepository {

    ShortUrl save(ShortUrl shortUrl);

    Optional<ShortUrl> findById(Long id);

    Optional<ShortUrl> findByShortCode(String shortCode);

    List<ShortUrl> findByCreatedBy(Long createdBy);

    Page<ShortUrl> findByCreatedBy(Long createdBy, Pageable pageable);

    List<ShortUrl> findByType(UrlType type);

    List<ShortUrl> findByStatus(UrlStatus status);

    Page<ShortUrl> findByStatus(UrlStatus status, Pageable pageable);

    List<ShortUrl> findByExpiresAtBefore(Instant now);

    List<ShortUrl> findByCreatedAtBetween(Instant start, Instant end);

    long countByCreatedBy(Long createdBy);

    long countByType(UrlType type);

    long countByStatus(UrlStatus status);
}