package com.marketplace.urlshortener.infrastructure.persistence.repository;

import com.marketplace.urlshortener.domain.valueobject.UrlStatus;
import com.marketplace.urlshortener.domain.valueobject.UrlType;
import com.marketplace.urlshortener.infrastructure.persistence.entity.ShortUrlJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShortUrlJpaRepository extends JpaRepository<ShortUrlJpaEntity, Long> {

    Optional<ShortUrlJpaEntity> findByShortCode(String shortCode);

    List<ShortUrlJpaEntity> findByCreatedBy(Long createdBy);

    Page<ShortUrlJpaEntity> findByCreatedBy(Long createdBy, Pageable pageable);

    List<ShortUrlJpaEntity> findByType(UrlType type);

    List<ShortUrlJpaEntity> findByStatus(UrlStatus status);

    Page<ShortUrlJpaEntity> findByStatus(UrlStatus status, Pageable pageable);

    List<ShortUrlJpaEntity> findByExpiresAtBefore(Instant now);

    List<ShortUrlJpaEntity> findByCreatedAtBetween(Instant start, Instant end);

    long countByCreatedBy(Long createdBy);

    long countByType(UrlType type);

    long countByStatus(UrlStatus status);
}