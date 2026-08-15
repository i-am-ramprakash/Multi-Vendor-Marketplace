package com.marketplace.urlshortener.domain.event;

import lombok.Getter;

@Getter
public class UrlClickedEvent extends DomainEvent {

    private final Long shortUrlId;
    private final String shortCode;
    private final String ipAddress;
    private final String referer;
    private final boolean isUnique;

    public UrlClickedEvent(Object source, Long shortUrlId, String shortCode, String ipAddress,
                          String referer, boolean isUnique) {
        super(source);
        this.shortUrlId = shortUrlId;
        this.shortCode = shortCode;
        this.ipAddress = ipAddress;
        this.referer = referer;
        this.isUnique = isUnique;
    }
}