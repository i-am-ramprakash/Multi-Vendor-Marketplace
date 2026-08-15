package com.marketplace.urlshortener.domain.repository;

import com.marketplace.urlshortener.domain.entity.UrlClick;

import java.time.Instant;
import java.util.List;

public interface UrlClickRepository {

    UrlClick save(UrlClick urlClick);

    List<UrlClick> findByShortUrlId(Long shortUrlId);

    List<UrlClick> findByShortCode(String shortCode);

    List<UrlClick> findByShortUrlIdAndClickedAtBetween(Long shortUrlId, Instant start, Instant end);

    long countByShortUrlId(Long shortUrlId);

    long countByShortUrlIdAndIsUnique(Long shortUrlId, boolean isUnique);

    long countByShortUrlIdAndClickedAtBetween(Long shortUrlId, Instant start, Instant end);
}