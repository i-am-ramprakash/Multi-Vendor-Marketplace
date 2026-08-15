package com.marketplace.urlshortener.infrastructure.persistence.repository;

import com.marketplace.urlshortener.domain.entity.UrlClick;
import com.marketplace.urlshortener.domain.repository.UrlClickRepository;
import com.marketplace.urlshortener.infrastructure.persistence.entity.UrlClickJpaEntity;
import com.marketplace.urlshortener.infrastructure.persistence.mapper.UrlClickPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UrlClickRepositoryImpl implements UrlClickRepository {

    private final UrlClickJpaRepository jpaRepository;

    @Override
    public UrlClick save(UrlClick urlClick) {
        UrlClickJpaEntity jpa = UrlClickPersistenceMapper.toJpaEntity(urlClick);
        UrlClickJpaEntity saved = jpaRepository.save(jpa);
        return UrlClickPersistenceMapper.toDomain(saved);
    }

    @Override
    public List<UrlClick> findByShortUrlId(Long shortUrlId) {
        return jpaRepository.findByShortUrlId(shortUrlId).stream()
            .map(UrlClickPersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<UrlClick> findByShortCode(String shortCode) {
        return jpaRepository.findByShortCode(shortCode).stream()
            .map(UrlClickPersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<UrlClick> findByShortUrlIdAndClickedAtBetween(Long shortUrlId, Instant start, Instant end) {
        return jpaRepository.findByShortUrlIdAndClickedAtBetween(shortUrlId, start, end).stream()
            .map(UrlClickPersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public long countByShortUrlId(Long shortUrlId) {
        return jpaRepository.countByShortUrlId(shortUrlId);
    }

    @Override
    public long countByShortUrlIdAndIsUnique(Long shortUrlId, boolean isUnique) {
        return jpaRepository.countByShortUrlIdAndIsUnique(shortUrlId, isUnique);
    }

    @Override
    public long countByShortUrlIdAndClickedAtBetween(Long shortUrlId, Instant start, Instant end) {
        return jpaRepository.countByShortUrlIdAndClickedAtBetween(shortUrlId, start, end);
    }
}