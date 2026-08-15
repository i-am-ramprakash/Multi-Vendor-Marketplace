package com.marketplace.auth.infrastructure.persistence.repository;

import com.marketplace.auth.domain.entity.Role;
import com.marketplace.auth.domain.repository.RoleRepository;
import com.marketplace.auth.infrastructure.persistence.entity.RoleJpaEntity;
import com.marketplace.auth.infrastructure.persistence.mapper.UserPersistenceMapper;
import com.marketplace.auth.infrastructure.persistence.repository.RoleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {

    private final RoleJpaRepository jpaRepository;

    @Override
    public Role save(Role role) {
        RoleJpaEntity jpaEntity = UserPersistenceMapper.toJpaEntity(role);
        RoleJpaEntity saved = jpaRepository.save(jpaEntity);
        return UserPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<Role> findById(Long id) {
        return jpaRepository.findById(id)
            .map(UserPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Role> findByName(String name) {
        return jpaRepository.findByName(name)
            .map(UserPersistenceMapper::toDomain);
    }

    @Override
    public List<Role> findAllById(Iterable<Long> ids) {
        return jpaRepository.findAllById(ids).stream()
            .map(UserPersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Role> findByNames(Set<String> names) {
        return jpaRepository.findByNameIn(names).stream()
            .map(UserPersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }
}