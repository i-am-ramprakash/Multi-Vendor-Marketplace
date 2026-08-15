package com.marketplace.auth.domain.valueobject;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PasswordHash {

    private String value;

    private PasswordHash(String value) {
        this.value = value;
    }

    public static PasswordHash of(String hashedValue) {
        if (hashedValue == null || hashedValue.isEmpty()) {
            throw new IllegalArgumentException("Password hash cannot be null or empty");
        }
        return new PasswordHash(hashedValue);
    }

    public static PasswordHash raw(String rawPassword, PasswordEncoder encoder) {
        return new PasswordHash(encoder.encode(rawPassword));
    }

    public boolean matches(String rawPassword, PasswordEncoder encoder) {
        return encoder.matches(rawPassword, this.value);
    }

    public interface PasswordEncoder {
        String encode(String rawPassword);
        boolean matches(String rawPassword, String encodedPassword);
    }

    @Override
    public String toString() {
        return "[PROTECTED]";
    }
}