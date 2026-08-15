package com.marketplace.auth.domain.repository;

import com.marketplace.auth.domain.entity.RefreshToken;
import com.marketplace.auth.domain.entity.User;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository {

    RefreshToken save(RefreshToken refreshToken);

    Optional<RefreshToken> findByToken(String token);

    List<RefreshToken> findByUser(User user);

    List<RefreshToken> findExpiredTokens(Instant before);

    void delete(RefreshToken refreshToken);

    void deleteAllByUser(User user);

    void revokeAllByUser(User user);
}