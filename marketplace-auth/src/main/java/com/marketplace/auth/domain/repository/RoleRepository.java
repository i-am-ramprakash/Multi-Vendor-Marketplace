package com.marketplace.auth.domain.repository;

import com.marketplace.auth.domain.entity.Role;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoleRepository {

    Role save(Role role);

    Optional<Role> findById(Long id);

    Optional<Role> findByName(String name);

    List<Role> findAllById(Iterable<Long> ids);

    List<Role> findByNames(Set<String> names);
}