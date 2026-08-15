package com.marketplace.shared.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Getter
@Slf4j
public class JwtTokenProvider {

    @Value("${app.jwt.secret:your-super-secret-jwt-key-that-is-at-least-256-bits-long-for-hs256}")
    private String secret;

    @Value("${app.jwt.access-token-expiry-minutes:60}")
    private long accessTokenExpiryMinutes;

    @Value("${app.jwt.refresh-token-expiry-days:30}")
    private long refreshTokenExpiryDays;

    @Value("${app.jwt.issuer:multivendor-marketplace}")
    private String issuer;

    @Value("${app.jwt.audience:multivendor-marketplace-api}")
    private String audience;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(Long userId, String email, Set<String> roles) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(accessTokenExpiryMinutes * 60);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("userId", userId)
                .claim("email", email)
                .claim("roles", roles)
                .claim("tokenType", "ACCESS")
                .issuer(issuer)
                .audience().add(audience).and()
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    public String generateRefreshToken(Long userId) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(refreshTokenExpiryDays * 24 * 60 * 60);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("userId", userId)
                .claim("tokenType", "REFRESH")
                .issuer(issuer)
                .audience().add(audience).and()
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    public Optional<JwtClaims> validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .requireIssuer(issuer)
                    .requireAudience(audience)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String tokenType = claims.get("tokenType", String.class);
            Instant expiresAt = claims.getExpiration().toInstant();

            if (expiresAt.isBefore(Instant.now())) {
                return Optional.empty();
            }

            Long userId = claims.get("userId", Long.class);
            String email = claims.get("email", String.class);
            Set<String> roles = claims.get("roles", Set.class);

            return Optional.of(new JwtClaims(userId, email, roles, tokenType, expiresAt));
        } catch (Exception e) {
            log.debug("Invalid JWT token: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public record JwtClaims(Long userId, String email, Set<String> roles, String tokenType, Instant expiresAt) {}
}