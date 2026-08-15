package com.marketplace.urlshortener.infrastructure.persistence.repository;

import com.marketplace.urlshortener.domain.entity.UrlAnalytics;
import com.marketplace.urlshortener.domain.repository.UrlAnalyticsRepository;
import com.marketplace.urlshortener.infrastructure.persistence.entity.UrlAnalyticsJpaEntity;
import com.marketplace.urlshortener.infrastructure.persistence.mapper.UrlAnalyticsPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UrlAnalyticsRepositoryImpl implements UrlAnalyticsRepository {

    private final UrlAnalyticsJpaRepository jpaRepository;

    @Override
    public UrlAnalytics save(UrlAnalytics urlAnalytics) {
        UrlAnalyticsJpaEntity jpa = UrlAnalyticsPersistenceMapper.toJpaEntity(urlAnalytics);
        UrlAnalyticsJpaEntity saved = jpaRepository.save(jpa);
        return UrlAnalyticsPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<UrlAnalytics> findByShortUrlId(Long shortUrlId) {
        return jpaRepository.findByShortUrlId(shortUrlId)
            .map(UrlAnalyticsPersistenceMapper::toDomain);
    }

    @Override
    public Optional<UrlAnalytics> findByShortCode(String shortCode) {
        return jpaRepository.findByShortCode(shortCode)
            .map(UrlAnalyticsPersistenceMapper::toDomain);
    }
}