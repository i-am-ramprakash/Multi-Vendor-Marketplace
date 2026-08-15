package com.marketplace.urlshortener.domain.entity;

import com.marketplace.urlshortener.domain.valueobject.ExpirationType;
import com.marketplace.urlshortener.domain.valueobject.UrlStatus;
import com.marketplace.urlshortener.domain.valueobject.UrlType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShortUrl {

    private Long id;
    private String shortCode;
    private String originalUrl;
    private String title;
    private String description;
    private UrlType type;
    private UrlStatus status;
    private ExpirationType expirationType;
    private Instant expiresAt;
    private Long referenceId;
    private String referenceType;
    private Long createdBy;
    private boolean isCustomAlias;
    private long clickCount;
    private long uniqueClickCount;
    private Instant lastClickedAt;
    private String password;
    private boolean requiresPassword;
    private String tags;
    private String metadata;
    private Instant createdAt;
    private Instant updatedAt;

    public ShortUrl(String shortCode, String originalUrl, UrlType type, Long createdBy) {
        this.shortCode = shortCode;
        this.originalUrl = originalUrl;
        this.type = type;
        this.status = UrlStatus.ACTIVE;
        this.expirationType = ExpirationType.NONE;
        this.createdBy = createdBy;
        this.isCustomAlias = false;
        this.clickCount = 0;
        this.uniqueClickCount = 0;
        this.requiresPassword = false;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void recordClick() {
        this.clickCount++;
        this.lastClickedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void recordUniqueClick() {
        this.uniqueClickCount++;
        this.recordClick();
    }

    public void deactivate() {
        this.status = UrlStatus.INACTIVE;
        this.updatedAt = Instant.now();
    }

    public void activate() {
        this.status = UrlStatus.ACTIVE;
        this.updatedAt = Instant.now();
    }

    public void ban() {
        this.status = UrlStatus.BANNED;
        this.updatedAt = Instant.now();
    }

    public boolean isExpired() {
        if (expirationType == ExpirationType.NONE || expiresAt == null) {
            return false;
        }
        return Instant.now().isAfter(expiresAt);
    }

    public boolean canBeAccessed() {
        return status == UrlStatus.ACTIVE && !isExpired();
    }

    public void setExpiration(ExpirationType type, Instant expiresAt) {
        this.expirationType = type;
        this.expiresAt = expiresAt;
        this.updatedAt = Instant.now();
    }

    public void setReference(Long referenceId, String referenceType) {
        this.referenceId = referenceId;
        this.referenceType = referenceType;
        this.updatedAt = Instant.now();
    }
}