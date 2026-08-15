package com.marketplace.urlshortener.infrastructure.persistence.repository;

import com.marketplace.urlshortener.domain.entity.ShortUrl;
import com.marketplace.urlshortener.domain.repository.ShortUrlRepository;
import com.marketplace.urlshortener.domain.valueobject.UrlStatus;
import com.marketplace.urlshortener.domain.valueobject.UrlType;
import com.marketplace.urlshortener.infrastructure.persistence.entity.ShortUrlJpaEntity;
import com.marketplace.urlshortener.infrastructure.persistence.mapper.ShortUrlPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ShortUrlRepositoryImpl implements ShortUrlRepository {

    private final ShortUrlJpaRepository jpaRepository;

    @Override
    public ShortUrl save(ShortUrl shortUrl) {
        ShortUrlJpaEntity jpa = ShortUrlPersistenceMapper.toJpaEntity(shortUrl);
        ShortUrlJpaEntity saved = jpaRepository.save(jpa);
        return ShortUrlPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<ShortUrl> findById(Long id) {
        return jpaRepository.findById(id)
            .map(ShortUrlPersistenceMapper::toDomain);
    }

    @Override
    public Optional<ShortUrl> findByShortCode(String shortCode) {
        return jpaRepository.findByShortCode(shortCode)
            .map(ShortUrlPersistenceMapper::toDomain);
    }

    @Override
    public List<ShortUrl> findByCreatedBy(Long createdBy) {
        return jpaRepository.findByCreatedBy(createdBy).stream()
            .map(ShortUrlPersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Page<ShortUrl> findByCreatedBy(Long createdBy, Pageable pageable) {
        return jpaRepository.findByCreatedBy(createdBy, pageable)
            .map(ShortUrlPersistenceMapper::toDomain);
    }

    @Override
    public List<ShortUrl> findByType(UrlType type) {
        return jpaRepository.findByType(type).stream()
            .map(ShortUrlPersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<ShortUrl> findByStatus(UrlStatus status) {
        return jpaRepository.findByStatus(status).stream()
            .map(ShortUrlPersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Page<ShortUrl> findByStatus(UrlStatus status, Pageable pageable) {
        return jpaRepository.findByStatus(status, pageable)
            .map(ShortUrlPersistenceMapper::toDomain);
    }

    @Override
    public List<ShortUrl> findByExpiresAtBefore(Instant now) {
        return jpaRepository.findByExpiresAtBefore(now).stream()
            .map(ShortUrlPersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<ShortUrl> findByCreatedAtBetween(Instant start, Instant end) {
        return jpaRepository.findByCreatedAtBetween(start, end).stream()
            .map(ShortUrlPersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public long countByCreatedBy(Long createdBy) {
        return jpaRepository.countByCreatedBy(createdBy);
    }

    @Override
    public long countByType(UrlType type) {
        return jpaRepository.countByType(type);
    }

    @Override
    public long countByStatus(UrlStatus status) {
        return jpaRepository.countByStatus(status);
    }
}