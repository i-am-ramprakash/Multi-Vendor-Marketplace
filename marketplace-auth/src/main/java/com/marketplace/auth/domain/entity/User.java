package com.marketplace.auth.domain.entity;

import com.marketplace.auth.domain.valueobject.Email;
import com.marketplace.auth.domain.valueobject.PasswordHash;
import com.marketplace.auth.domain.valueobject.PhoneNumber;
import com.marketplace.auth.domain.valueobject.UserStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    private Long id;
    private String publicId;
    private Email email;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private PhoneNumber phone;
    private String avatarUrl;
    private UserStatus status;
    private boolean emailVerified;
    private Instant lastLoginAt;
    private Instant createdAt;
    private Instant updatedAt;
    private Long version;
    private Set<Role> roles = new HashSet<>();

    public User(Email email, PasswordHash passwordHash, String firstName, String lastName) {
        this.publicId = UUID.randomUUID().toString();
        this.email = email;
        this.passwordHash = passwordHash.getValue();
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = UserStatus.ACTIVE;
        this.emailVerified = false;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
    }

    public boolean hasRole(String roleName) {
        return roles.stream().anyMatch(r -> r.getName().equals(roleName));
    }

    public void updateProfile(String firstName, String lastName, PhoneNumber phone, String avatarUrl) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.avatarUrl = avatarUrl;
    }

    public void changePassword(PasswordHash newPasswordHash) {
        this.passwordHash = newPasswordHash.getValue();
    }

    public void verifyEmail() {
        this.emailVerified = true;
    }

    public void recordLogin() {
        this.lastLoginAt = Instant.now();
    }

    public void activate() {
        this.status = UserStatus.ACTIVE;
    }

    public void deactivate() {
        this.status = UserStatus.INACTIVE;
    }

    public void suspend() {
        this.status = UserStatus.SUSPENDED;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}