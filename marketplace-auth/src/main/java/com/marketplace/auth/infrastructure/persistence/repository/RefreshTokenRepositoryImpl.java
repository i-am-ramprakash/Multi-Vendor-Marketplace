package com.marketplace.auth.infrastructure.persistence.repository;

import com.marketplace.auth.domain.entity.RefreshToken;
import com.marketplace.auth.domain.entity.User;
import com.marketplace.auth.domain.repository.RefreshTokenRepository;
import com.marketplace.auth.infrastructure.persistence.entity.RefreshTokenJpaEntity;
import com.marketplace.auth.infrastructure.persistence.mapper.RefreshTokenPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository jpaRepository;

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        RefreshTokenJpaEntity jpaEntity = RefreshTokenPersistenceMapper.toJpaEntity(refreshToken);
        RefreshTokenJpaEntity saved = jpaRepository.save(jpaEntity);
        return RefreshTokenPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return jpaRepository.findByToken(token)
            .map(RefreshTokenPersistenceMapper::toDomain);
    }

    @Override
    public List<RefreshToken> findByUser(User user) {
        return jpaRepository.findByUserId(user.getId()).stream()
            .map(RefreshTokenPersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<RefreshToken> findExpiredTokens(Instant before) {
        return jpaRepository.findByExpiresAtBefore(before).stream()
            .map(RefreshTokenPersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public void delete(RefreshToken refreshToken) {
        jpaRepository.delete(RefreshTokenPersistenceMapper.toJpaEntity(refreshToken));
    }

    @Override
    public void deleteAllByUser(User user) {
        jpaRepository.deleteByUserId(user.getId());
    }

    @Override
    public void revokeAllByUser(User user) {
        jpaRepository.revokeAllByUserId(user.getId());
    }
}