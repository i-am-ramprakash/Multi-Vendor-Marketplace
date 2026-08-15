package com.marketplace.urlshortener.infrastructure.persistence.repository;

import com.marketplace.urlshortener.infrastructure.persistence.entity.UrlRateLimitJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRateLimitJpaRepository extends JpaRepository<UrlRateLimitJpaEntity, Long> {

    Optional<UrlRateLimitJpaEntity> findByIdentifierAndType(String identifier, String type);
}