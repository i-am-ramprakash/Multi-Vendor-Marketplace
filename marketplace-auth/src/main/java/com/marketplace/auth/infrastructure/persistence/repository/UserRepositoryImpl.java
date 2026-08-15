package com.marketplace.auth.infrastructure.persistence.repository;

import com.marketplace.auth.domain.entity.User;
import com.marketplace.auth.domain.repository.UserRepository;
import com.marketplace.auth.domain.valueobject.Email;
import com.marketplace.auth.infrastructure.persistence.entity.UserJpaEntity;
import com.marketplace.auth.infrastructure.persistence.mapper.UserPersistenceMapper;
import com.marketplace.auth.infrastructure.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository jpaRepository;

    @Override
    public User save(User user) {
        UserJpaEntity jpaEntity = UserPersistenceMapper.toJpaEntity(user);
        UserJpaEntity saved = jpaRepository.save(jpaEntity);
        return UserPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpaRepository.findById(id)
            .map(UserPersistenceMapper::toDomain);
    }

    @Override
    public Optional<User> findByPublicId(String publicId) {
        return jpaRepository.findByPublicId(publicId)
            .map(UserPersistenceMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return jpaRepository.findByEmail(email.getValue())
            .map(UserPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(Email email) {
        return jpaRepository.existsByEmail(email.getValue());
    }

    @Override
    public void delete(User user) {
        jpaRepository.delete(UserPersistenceMapper.toJpaEntity(user));
    }
}