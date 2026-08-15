package com.marketplace.urlshortener.infrastructure.persistence.repository;

import com.marketplace.urlshortener.infrastructure.persistence.entity.UrlClickJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface UrlClickJpaRepository extends JpaRepository<UrlClickJpaEntity, Long> {

    List<UrlClickJpaEntity> findByShortUrlId(Long shortUrlId);

    List<UrlClickJpaEntity> findByShortCode(String shortCode);

    List<UrlClickJpaEntity> findByShortUrlIdAndClickedAtBetween(Long shortUrlId, Instant start, Instant end);

    long countByShortUrlId(Long shortUrlId);

    long countByShortUrlIdAndIsUnique(Long shortUrlId, boolean isUnique);

    long countByShortUrlIdAndClickedAtBetween(Long shortUrlId, Instant start, Instant end);
}