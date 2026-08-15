package com.marketplace.urlshortener.infrastructure.persistence.mapper;

import com.marketplace.urlshortener.domain.entity.UrlClick;
import com.marketplace.urlshortener.infrastructure.persistence.entity.UrlClickJpaEntity;

import java.lang.reflect.Field;

public final class UrlClickPersistenceMapper {

    private UrlClickPersistenceMapper() {}

    public static UrlClickJpaEntity toJpaEntity(UrlClick domain) {
        if (domain == null) return null;

        UrlClickJpaEntity jpa = new UrlClickJpaEntity();
        jpa.setId(domain.getId());
        jpa.setShortUrlId(domain.getShortUrlId());
        jpa.setShortCode(domain.getShortCode());
        jpa.setIpAddress(domain.getIpAddress());
        jpa.setUserAgent(domain.getUserAgent());
        jpa.setReferer(domain.getReferer());
        jpa.setCountry(domain.getCountry());
        jpa.setCity(domain.getCity());
        jpa.setDevice(domain.getDevice());
        jpa.setBrowser(domain.getBrowser());
        jpa.setOs(domain.getOs());
        jpa.setUnique(domain.isUnique());
        jpa.setUserId(domain.getUserId());
        jpa.setClickedAt(domain.getClickedAt());

        return jpa;
    }

    public static UrlClick toDomain(UrlClickJpaEntity jpa) {
        if (jpa == null) return null;

        UrlClick click = new UrlClick(
            jpa.getShortUrlId(),
            jpa.getShortCode(),
            jpa.getIpAddress(),
            jpa.getUserAgent(),
            jpa.getReferer(),
            jpa.isUnique(),
            jpa.getUserId()
        );
        setId(click, jpa.getId());
        click.setCountry(jpa.getCountry());
        click.setCity(jpa.getCity());
        click.setDevice(jpa.getDevice());
        click.setBrowser(jpa.getBrowser());
        click.setOs(jpa.getOs());
        click.setClickedAt(jpa.getClickedAt());

        return click;
    }

    private static void setId(UrlClick click, Long id) {
        try {
            Field field = UrlClick.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(click, id);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot set URL click ID", e);
        }
    }
}