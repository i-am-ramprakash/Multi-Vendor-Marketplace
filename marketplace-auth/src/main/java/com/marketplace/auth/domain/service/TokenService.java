package com.marketplace.auth.domain.service;

import com.marketplace.auth.domain.entity.User;

import java.time.Instant;
import java.util.Optional;

public interface TokenService {

    String generateAccessToken(User user);

    String generateRefreshToken(User user);

    TokenClaims parseAccessToken(String token);

    TokenClaims parseRefreshToken(String token);

    Optional<TokenClaims> validateAccessToken(String token);

    Optional<TokenClaims> validateRefreshToken(String token);

    Instant getAccessTokenExpiry();

    Instant getRefreshTokenExpiry();

    String generateEmailVerificationToken(User user);

    Optional<Long> parseEmailVerificationToken(String token);

    String generatePasswordResetToken(User user);

    Optional<Long> parsePasswordResetToken(String token);

    record TokenClaims(
        Long userId,
        String email,
        String role,
        Instant issuedAt,
        Instant expiresAt,
        String tokenType
    ) {}
}