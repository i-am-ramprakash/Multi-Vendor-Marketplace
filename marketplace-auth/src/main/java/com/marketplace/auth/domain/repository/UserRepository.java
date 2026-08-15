package com.marketplace.auth.domain.repository;

import com.marketplace.auth.domain.entity.User;
import com.marketplace.auth.domain.valueobject.Email;

import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByPublicId(String publicId);

    Optional<User> findByEmail(Email email);

    boolean existsByEmail(Email email);

    void delete(User user);
}