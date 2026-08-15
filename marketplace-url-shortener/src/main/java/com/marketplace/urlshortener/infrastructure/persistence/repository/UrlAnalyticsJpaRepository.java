package com.marketplace.urlshortener.infrastructure.persistence.repository;

import com.marketplace.urlshortener.infrastructure.persistence.entity.UrlAnalyticsJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlAnalyticsJpaRepository extends JpaRepository<UrlAnalyticsJpaEntity, Long> {

    Optional<UrlAnalyticsJpaEntity> findByShortUrlId(Long shortUrlId);

    Optional<UrlAnalyticsJpaEntity> findByShortCode(String shortCode);
}