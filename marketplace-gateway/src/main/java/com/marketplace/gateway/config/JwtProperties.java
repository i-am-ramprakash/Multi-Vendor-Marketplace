package com.marketplace.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {

    private String secret = "your-super-secret-jwt-key-that-is-at-least-256-bits-long-for-hs256";
    private String issuer = "multivendor-marketplace";
    private String audience = "multivendor-marketplace-api";
    private long accessTokenExpiryMinutes = 60;
}
