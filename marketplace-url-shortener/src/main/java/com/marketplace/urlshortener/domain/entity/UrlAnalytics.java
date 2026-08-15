package com.marketplace.urlshortener.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UrlAnalytics {

    private Long id;
    private Long shortUrlId;
    private String shortCode;
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
    private Instant createdAt;

    public UrlAnalytics(Long shortUrlId, String shortCode) {
        this.shortUrlId = shortUrlId;
        this.shortCode = shortCode;
        this.totalClicks = 0;
        this.uniqueVisitors = 0;
        this.clicksToday = 0;
        this.clicksThisWeek = 0;
        this.clicksThisMonth = 0;
        this.lastUpdated = Instant.now();
        this.createdAt = Instant.now();
    }

    public void incrementClicks(boolean isUnique) {
        this.totalClicks++;
        this.clicksToday++;
        this.clicksThisWeek++;
        this.clicksThisMonth++;
        if (isUnique) {
            this.uniqueVisitors++;
        }
        this.lastUpdated = Instant.now();
    }

    public void updateTopMetrics(String country, String city, String device, String browser, String referer) {
        this.topCountry = country;
        this.topCity = city;
        this.topDevice = device;
        this.topBrowser = browser;
        this.topReferer = referer;
        this.lastUpdated = Instant.now();
    }

    public void resetDailyCounters() {
        this.clicksToday = 0;
        this.lastUpdated = Instant.now();
    }

    public void resetWeeklyCounters() {
        this.clicksThisWeek = 0;
        this.lastUpdated = Instant.now();
    }

    public void resetMonthlyCounters() {
        this.clicksThisMonth = 0;
        this.lastUpdated = Instant.now();
    }
}