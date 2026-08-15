package com.marketplace.auth.infrastructure.security.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.jwt")
@Data
public class JwtProperties {

    private String secret;
    private long accessTokenExpiryMinutes = 60;
    private long refreshTokenExpiryDays = 30;
    private long refreshTokenExpiryDaysRememberMe = 90;
    private String issuer = "multivendor-marketplace";
    private String audience = "multivendor-marketplace-api";
}