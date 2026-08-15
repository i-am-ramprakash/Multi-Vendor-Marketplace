package com.marketplace.urlshortener.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "url_analytics", indexes = {
    @Index(name = "idx_url_analytics_short_url_id", columnList = "short_url_id", unique = true),
    @Index(name = "idx_url_analytics_short_code", columnList = "short_code", unique = true)
})
@Data
@NoArgsConstructor
public class UrlAnalyticsJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "short_url_id", nullable = false, unique = true)
    private Long shortUrlId;

    @Column(name = "short_code", nullable = false, unique = true, length = 20)
    private String shortCode;

    @Column(name = "total_clicks", nullable = false)
    private long totalClicks;

    @Column(name = "unique_visitors", nullable = false)
    private long uniqueVisitors;

    @Column(name = "clicks_today", nullable = false)
    private long clicksToday;

    @Column(name = "clicks_this_week", nullable = false)
    private long clicksThisWeek;

    @Column(name = "clicks_this_month", nullable = false)
    private long clicksThisMonth;

    @Column(name = "top_country", length = 100)
    private String topCountry;

    @Column(name = "top_city", length = 100)
    private String topCity;

    @Column(name = "top_device", length = 50)
    private String topDevice;

    @Column(name = "top_browser", length = 50)
    private String topBrowser;

    @Column(name = "top_referer", columnDefinition = "TEXT")
    private String topReferer;

    @Column(name = "last_updated", nullable = false)
    private Instant lastUpdated;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}