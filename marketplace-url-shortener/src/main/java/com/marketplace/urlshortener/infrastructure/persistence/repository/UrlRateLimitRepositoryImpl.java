package com.marketplace.urlshortener.infrastructure.persistence.repository;

import com.marketplace.urlshortener.domain.entity.UrlRateLimit;
import com.marketplace.urlshortener.domain.repository.UrlRateLimitRepository;
import com.marketplace.urlshortener.infrastructure.persistence.entity.UrlRateLimitJpaEntity;
import com.marketplace.urlshortener.infrastructure.persistence.mapper.UrlRateLimitPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UrlRateLimitRepositoryImpl implements UrlRateLimitRepository {

    private final UrlRateLimitJpaRepository jpaRepository;

    @Override
    public UrlRateLimit save(UrlRateLimit urlRateLimit) {
        UrlRateLimitJpaEntity jpa = UrlRateLimitPersistenceMapper.toJpaEntity(urlRateLimit);
        UrlRateLimitJpaEntity saved = jpaRepository.save(jpa);
        return UrlRateLimitPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<UrlRateLimit> findByIdentifierAndType(String identifier, String type) {
        return jpaRepository.findByIdentifierAndType(identifier, type)
            .map(UrlRateLimitPersistenceMapper::toDomain);
    }
}