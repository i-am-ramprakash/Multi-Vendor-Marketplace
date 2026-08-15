package com.marketplace.urlshortener.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UrlAnalyticsResponse {

    private Long shortUrlId;
    private String shortCode;
    private String originalUrl;
    private long totalClicks;
    private long uniqueVisitors;
    private long clicksToday;
    private long clicksThisWeek;
    private long clicksThisMonth;
    private String topCountry;
    private String topCity;
    private String topDevice;
    private String topBrowser;
    private String topReferer;
    private Instant lastUpdated;

    public static UrlAnalyticsResponse from(com.marketplace.urlshortener.domain.entity.UrlAnalytics analytics) {
        return UrlAnalyticsResponse.builder()
            .shortUrlId(analytics.getShortUrlId())
            .shortCode(analytics.getShortCode())
            .totalClicks(analytics.getTotalClicks())
            .uniqueVisitors(analytics.getUniqueVisitors())
            .clicksToday(analytics.getClicksToday())
            .clicksThisWeek(analytics.getClicksThisWeek())
            .clicksThisMonth(analytics.getClicksThisMonth())
            .topCountry(analytics.getTopCountry())
            .topCity(analytics.getTopCity())
            .topDevice(analytics.getTopDevice())
            .topBrowser(analytics.getTopBrowser())
            .topReferer(analytics.getTopReferer())
            .lastUpdated(analytics.getLastUpdated())
            .build();
    }
}