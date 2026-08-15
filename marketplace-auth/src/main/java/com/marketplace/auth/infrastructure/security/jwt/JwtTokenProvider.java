package com.marketplace.auth.infrastructure.security.jwt;

import com.marketplace.auth.domain.entity.User;
import com.marketplace.auth.domain.service.TokenService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider implements TokenService {

    private final JwtProperties jwtProperties;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        byte[] keyBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String generateAccessToken(User user) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(getAccessTokenExpirySeconds());

        Set<String> roles = user.getRoles().stream()
            .map(role -> role.getName())
            .collect(Collectors.toSet());

        return Jwts.builder()
            .subject(user.getPublicId())
            .claim("userId", user.getId())
            .claim("email", user.getEmail().getValue())
            .claim("roles", roles)
            .claim("tokenType", "ACCESS")
            .issuer(jwtProperties.getIssuer())
            .audience().add(jwtProperties.getAudience()).and()
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiry))
            .signWith(secretKey, Jwts.SIG.HS256)
            .compact();
    }

    @Override
    public String generateRefreshToken(User user) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(getRefreshTokenExpirySeconds());

        return Jwts.builder()
            .subject(user.getPublicId())
            .claim("userId", user.getId())
            .claim("tokenType", "REFRESH")
            .issuer(jwtProperties.getIssuer())
            .audience().add(jwtProperties.getAudience()).and()
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiry))
            .signWith(secretKey, Jwts.SIG.HS256)
            .compact();
    }

    @Override
    public TokenClaims parseAccessToken(String token) {
        return parseToken(token, "ACCESS");
    }

    @Override
    public TokenClaims parseRefreshToken(String token) {
        return parseToken(token, "REFRESH");
    }

    @Override
    public Optional<TokenClaims> validateAccessToken(String token) {
        return validateToken(token, "ACCESS");
    }

    @Override
    public Optional<TokenClaims> validateRefreshToken(String token) {
        return validateToken(token, "REFRESH");
    }

    @Override
    public Instant getAccessTokenExpiry() {
        return Instant.now().plusSeconds(getAccessTokenExpirySeconds());
    }

    @Override
    public Instant getRefreshTokenExpiry() {
        return Instant.now().plusSeconds(getRefreshTokenExpirySeconds());
    }

    @Override
    public String generateEmailVerificationToken(User user) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(24 * 60 * 60); // 24 hours

        return Jwts.builder()
            .subject(user.getPublicId())
            .claim("userId", user.getId())
            .claim("email", user.getEmail().getValue())
            .claim("tokenType", "EMAIL_VERIFICATION")
            .issuer(jwtProperties.getIssuer())
            .audience().add(jwtProperties.getAudience()).and()
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiry))
            .signWith(secretKey, Jwts.SIG.HS256)
            .compact();
    }

    @Override
    public Optional<Long> parseEmailVerificationToken(String token) {
        try {
            Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .requireIssuer(jwtProperties.getIssuer())
                .requireAudience(jwtProperties.getAudience())
                .build()
                .parseSignedClaims(token)
                .getPayload();

            if (!"EMAIL_VERIFICATION".equals(claims.get("tokenType"))) {
                return Optional.empty();
            }

            return Optional.of(claims.get("userId", Long.class));
        } catch (JwtException e) {
            log.debug("Invalid email verification token: {}", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public String generatePasswordResetToken(User user) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(60 * 60); // 1 hour

        return Jwts.builder()
            .subject(user.getPublicId())
            .claim("userId", user.getId())
            .claim("email", user.getEmail().getValue())
            .claim("tokenType", "PASSWORD_RESET")
            .issuer(jwtProperties.getIssuer())
            .audience().add(jwtProperties.getAudience()).and()
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiry))
            .signWith(secretKey, Jwts.SIG.HS256)
            .compact();
    }

    @Override
    public Optional<Long> parsePasswordResetToken(String token) {
        try {
            Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .requireIssuer(jwtProperties.getIssuer())
                .requireAudience(jwtProperties.getAudience())
                .build()
                .parseSignedClaims(token)
                .getPayload();

            if (!"PASSWORD_RESET".equals(claims.get("tokenType"))) {
                return Optional.empty();
            }

            return Optional.of(claims.get("userId", Long.class));
        } catch (JwtException e) {
            log.debug("Invalid password reset token: {}", e.getMessage());
            return Optional.empty();
        }
    }

    private long getAccessTokenExpirySeconds() {
        return jwtProperties.getAccessTokenExpiryMinutes() * 60;
    }

    private long getRefreshTokenExpirySeconds() {
        return jwtProperties.getRefreshTokenExpiryDays() * 24 * 60 * 60;
    }

    private TokenClaims parseToken(String token, String expectedType) {
        try {
            Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .requireIssuer(jwtProperties.getIssuer())
                .requireAudience(jwtProperties.getAudience())
                .build()
                .parseSignedClaims(token)
                .getPayload();

            String tokenType = claims.get("tokenType", String.class);
            if (!expectedType.equals(tokenType)) {
                throw new JwtException("Invalid token type");
            }

            return new TokenClaims(
                claims.get("userId", Long.class),
                claims.get("email", String.class),
                (String) claims.get("roles", Set.class).stream().findFirst().orElse(null),
                claims.getIssuedAt().toInstant(),
                claims.getExpiration().toInstant(),
                tokenType
            );
        } catch (JwtException e) {
            log.debug("Failed to parse {} token: {}", expectedType, e.getMessage());
            throw e;
        }
    }

    private Optional<TokenClaims> validateToken(String token, String expectedType) {
        try {
            TokenClaims claims = parseToken(token, expectedType);
            if (claims.expiresAt().isBefore(Instant.now())) {
                return Optional.empty();
            }
            return Optional.of(claims);
        } catch (JwtException e) {
            return Optional.empty();
        }
    }
}