package com.marketplace.auth.infrastructure.persistence.mapper;

import com.marketplace.auth.domain.entity.User;
import com.marketplace.auth.domain.entity.Role;
import com.marketplace.auth.domain.valueobject.Email;
import com.marketplace.auth.domain.valueobject.PasswordHash;
import com.marketplace.auth.domain.valueobject.PhoneNumber;
import com.marketplace.auth.infrastructure.persistence.entity.UserJpaEntity;
import com.marketplace.auth.infrastructure.persistence.entity.RoleJpaEntity;

import java.util.Set;
import java.util.stream.Collectors;

public final class UserPersistenceMapper {

    private UserPersistenceMapper() {}

    public static UserJpaEntity toJpaEntity(User domain) {
        UserJpaEntity jpa = new UserJpaEntity();
        jpa.setId(domain.getId());
        jpa.setPublicId(domain.getPublicId());
        jpa.setEmail(domain.getEmail().getValue());
        jpa.setPasswordHash(domain.getPasswordHash());
        jpa.setFirstName(domain.getFirstName());
        jpa.setLastName(domain.getLastName());
        jpa.setPhone(domain.getPhone() != null ? domain.getPhone().getValue() : null);
        jpa.setAvatarUrl(domain.getAvatarUrl());
        jpa.setStatus(domain.getStatus());
        jpa.setEmailVerified(domain.isEmailVerified());
        jpa.setLastLoginAt(domain.getLastLoginAt());
        jpa.setCreatedAt(domain.getCreatedAt());
        jpa.setUpdatedAt(domain.getUpdatedAt());
        jpa.setVersion(domain.getVersion());
        jpa.setRoles(domain.getRoles().stream()
            .map(UserPersistenceMapper::toJpaEntity)
            .collect(Collectors.toSet()));
        return jpa;
    }

    public static User toDomain(UserJpaEntity jpa) {
        if (jpa == null) {
            return null;
        }
        User user = new User(
            Email.of(jpa.getEmail()),
            PasswordHash.of(jpa.getPasswordHash()),
            jpa.getFirstName(),
            jpa.getLastName()
        );
        // Use reflection or package-private setter for ID
        setId(user, jpa.getId());
        user.setPublicId(jpa.getPublicId());
        user.setPhone(jpa.getPhone() != null ? PhoneNumber.of(jpa.getPhone()) : PhoneNumber.empty());
        user.setAvatarUrl(jpa.getAvatarUrl());
        user.setStatus(jpa.getStatus());
        user.setEmailVerified(jpa.isEmailVerified());
        user.setLastLoginAt(jpa.getLastLoginAt());
        user.setCreatedAt(jpa.getCreatedAt());
        user.setUpdatedAt(jpa.getUpdatedAt());
        user.setVersion(jpa.getVersion());
        
        Set<Role> roles = jpa.getRoles().stream()
            .map(UserPersistenceMapper::toDomain)
            .collect(Collectors.toSet());
        roles.forEach(user::addRole);
        
        return user;
    }

    public static RoleJpaEntity toJpaEntity(Role domain) {
        RoleJpaEntity jpa = new RoleJpaEntity();
        jpa.setId(domain.getId());
        jpa.setName(domain.getName());
        jpa.setDescription(domain.getDescription());
        jpa.setCreatedAt(domain.getCreatedAt());
        return jpa;
    }

    public static Role toDomain(RoleJpaEntity jpa) {
        if (jpa == null) {
            return null;
        }
        Role role = new Role(jpa.getName(), jpa.getDescription());
        setId(role, jpa.getId());
        role.setCreatedAt(jpa.getCreatedAt());
        return role;
    }

    private static void setId(User user, Long id) {
        try {
            java.lang.reflect.Field field = User.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(user, id);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot set user ID", e);
        }
    }

    private static void setId(Role role, Long id) {
        try {
            java.lang.reflect.Field field = Role.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(role, id);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot set role ID", e);
        }
    }
}