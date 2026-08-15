package com.marketplace.urlshortener.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UrlClick {

    private Long id;
    private Long shortUrlId;
    private String shortCode;
    private String ipAddress;
    private String userAgent;
    private String referer;
    private String country;
    private String city;
    private String device;
    private String browser;
    private String os;
    private boolean isUnique;
    private Long userId;
    private Instant clickedAt;

    public UrlClick(Long shortUrlId, String shortCode, String ipAddress, String userAgent,
                   String referer, boolean isUnique, Long userId) {
        this.shortUrlId = shortUrlId;
        this.shortCode = shortCode;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.referer = referer;
        this.isUnique = isUnique;
        this.userId = userId;
        this.clickedAt = Instant.now();
    }

    public void setGeoLocation(String country, String city) {
        this.country = country;
        this.city = city;
    }

    public void setDeviceInfo(String device, String browser, String os) {
        this.device = device;
        this.browser = browser;
        this.os = os;
    }
}