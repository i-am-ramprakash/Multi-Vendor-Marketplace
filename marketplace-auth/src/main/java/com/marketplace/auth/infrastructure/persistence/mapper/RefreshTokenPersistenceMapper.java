package com.marketplace.auth.infrastructure.persistence.mapper;

import com.marketplace.auth.domain.entity.RefreshToken;
import com.marketplace.auth.domain.entity.User;
import com.marketplace.auth.infrastructure.persistence.entity.RefreshTokenJpaEntity;
import com.marketplace.auth.infrastructure.persistence.entity.UserJpaEntity;

public final class RefreshTokenPersistenceMapper {

    private RefreshTokenPersistenceMapper() {}

    public static RefreshTokenJpaEntity toJpaEntity(RefreshToken domain) {
        RefreshTokenJpaEntity jpa = new RefreshTokenJpaEntity();
        jpa.setId(domain.getId());
        jpa.setUser(UserPersistenceMapper.toJpaEntity(domain.getUser()));
        jpa.setToken(domain.getToken());
        jpa.setExpiresAt(domain.getExpiresAt());
        jpa.setRevoked(domain.isRevoked());
        jpa.setCreatedAt(domain.getCreatedAt());
        return jpa;
    }

    public static RefreshToken toDomain(RefreshTokenJpaEntity jpa) {
        if (jpa == null) {
            return null;
        }
        RefreshToken token = new RefreshToken(
            UserPersistenceMapper.toDomain(jpa.getUser()),
            jpa.getToken(),
            jpa.getExpiresAt()
        );
        setId(token, jpa.getId());
        token.setRevoked(jpa.isRevoked());
        token.setCreatedAt(jpa.getCreatedAt());
        return token;
    }

    private static void setId(RefreshToken token, Long id) {
        try {
            java.lang.reflect.Field field = RefreshToken.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(token, id);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot set refresh token ID", e);
        }
    }
}