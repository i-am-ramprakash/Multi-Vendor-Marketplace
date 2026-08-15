package com.marketplace.urlshortener.infrastructure.persistence.mapper;

import com.marketplace.urlshortener.domain.entity.ShortUrl;
import com.marketplace.urlshortener.infrastructure.persistence.entity.ShortUrlJpaEntity;

import java.lang.reflect.Field;

public final class ShortUrlPersistenceMapper {

    private ShortUrlPersistenceMapper() {}

    public static ShortUrlJpaEntity toJpaEntity(ShortUrl domain) {
        if (domain == null) return null;

        ShortUrlJpaEntity jpa = new ShortUrlJpaEntity();
        jpa.setId(domain.getId());
        jpa.setShortCode(domain.getShortCode());
        jpa.setOriginalUrl(domain.getOriginalUrl());
        jpa.setTitle(domain.getTitle());
        jpa.setDescription(domain.getDescription());
        jpa.setType(domain.getType());
        jpa.setStatus(domain.getStatus());
        jpa.setExpirationType(domain.getExpirationType());
        jpa.setExpiresAt(domain.getExpiresAt());
        jpa.setReferenceId(domain.getReferenceId());
        jpa.setReferenceType(domain.getReferenceType());
        jpa.setCreatedBy(domain.getCreatedBy());
        jpa.setCustomAlias(domain.isCustomAlias());
        jpa.setClickCount(domain.getClickCount());
        jpa.setUniqueClickCount(domain.getUniqueClickCount());
        jpa.setLastClickedAt(domain.getLastClickedAt());
        jpa.setPassword(domain.getPassword());
        jpa.setRequiresPassword(domain.isRequiresPassword());
        jpa.setTags(domain.getTags());
        jpa.setMetadata(domain.getMetadata());
        jpa.setCreatedAt(domain.getCreatedAt());
        jpa.setUpdatedAt(domain.getUpdatedAt());

        return jpa;
    }

    public static ShortUrl toDomain(ShortUrlJpaEntity jpa) {
        if (jpa == null) return null;

        ShortUrl shortUrl = new ShortUrl(jpa.getShortCode(), jpa.getOriginalUrl(), jpa.getType(), jpa.getCreatedBy());
        setId(shortUrl, jpa.getId());
        shortUrl.setTitle(jpa.getTitle());
        shortUrl.setDescription(jpa.getDescription());
        shortUrl.setStatus(jpa.getStatus());
        shortUrl.setExpirationType(jpa.getExpirationType());
        shortUrl.setExpiresAt(jpa.getExpiresAt());
        shortUrl.setReferenceId(jpa.getReferenceId());
        shortUrl.setReferenceType(jpa.getReferenceType());
        shortUrl.setCustomAlias(jpa.isCustomAlias());
        shortUrl.setClickCount(jpa.getClickCount());
        shortUrl.setUniqueClickCount(jpa.getUniqueClickCount());
        shortUrl.setLastClickedAt(jpa.getLastClickedAt());
        shortUrl.setPassword(jpa.getPassword());
        shortUrl.setRequiresPassword(jpa.isRequiresPassword());
        shortUrl.setTags(jpa.getTags());
        shortUrl.setMetadata(jpa.getMetadata());
        shortUrl.setCreatedAt(jpa.getCreatedAt());
        shortUrl.setUpdatedAt(jpa.getUpdatedAt());

        return shortUrl;
    }

    private static void setId(ShortUrl shortUrl, Long id) {
        try {
            Field field = ShortUrl.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(shortUrl, id);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot set short URL ID", e);
        }
    }
}