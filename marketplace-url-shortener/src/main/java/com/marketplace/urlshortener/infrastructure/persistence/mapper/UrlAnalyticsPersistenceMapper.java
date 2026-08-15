package com.marketplace.urlshortener.infrastructure.persistence.mapper;

import com.marketplace.urlshortener.domain.entity.UrlAnalytics;
import com.marketplace.urlshortener.infrastructure.persistence.entity.UrlAnalyticsJpaEntity;

import java.lang.reflect.Field;

public final class UrlAnalyticsPersistenceMapper {

    private UrlAnalyticsPersistenceMapper() {}

    public static UrlAnalyticsJpaEntity toJpaEntity(UrlAnalytics domain) {
        if (domain == null) return null;

        UrlAnalyticsJpaEntity jpa = new UrlAnalyticsJpaEntity();
        jpa.setId(domain.getId());
        jpa.setShortUrlId(domain.getShortUrlId());
        jpa.setShortCode(domain.getShortCode());
        jpa.setTotalClicks(domain.getTotalClicks());
        jpa.setUniqueVisitors(domain.getUniqueVisitors());
        jpa.setClicksToday(domain.getClicksToday());
        jpa.setClicksThisWeek(domain.getClicksThisWeek());
        jpa.setClicksThisMonth(domain.getClicksThisMonth());
        jpa.setTopCountry(domain.getTopCountry());
        jpa.setTopCity(domain.getTopCity());
        jpa.setTopDevice(domain.getTopDevice());
        jpa.setTopBrowser(domain.getTopBrowser());
        jpa.setTopReferer(domain.getTopReferer());
        jpa.setLastUpdated(domain.getLastUpdated());
        jpa.setCreatedAt(domain.getCreatedAt());

        return jpa;
    }

    public static UrlAnalytics toDomain(UrlAnalyticsJpaEntity jpa) {
        if (jpa == null) return null;

        UrlAnalytics analytics = new UrlAnalytics(jpa.getShortUrlId(), jpa.getShortCode());
        setId(analytics, jpa.getId());
        analytics.setTotalClicks(jpa.getTotalClicks());
        analytics.setUniqueVisitors(jpa.getUniqueVisitors());
        analytics.setClicksToday(jpa.getClicksToday());
        analytics.setClicksThisWeek(jpa.getClicksThisWeek());
        analytics.setClicksThisMonth(jpa.getClicksThisMonth());
        analytics.setTopCountry(jpa.getTopCountry());
        analytics.setTopCity(jpa.getTopCity());
        analytics.setTopDevice(jpa.getTopDevice());
        analytics.setTopBrowser(jpa.getTopBrowser());
        analytics.setTopReferer(jpa.getTopReferer());
        analytics.setLastUpdated(jpa.getLastUpdated());
        analytics.setCreatedAt(jpa.getCreatedAt());

        return analytics;
    }

    private static void setId(UrlAnalytics analytics, Long id) {
        try {
            Field field = UrlAnalytics.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(analytics, id);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot set URL analytics ID", e);
        }
    }
}