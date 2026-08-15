package com.marketplace.auth.domain.service;

import com.marketplace.auth.domain.valueobject.PasswordHash;

public interface PasswordService {

    PasswordHash hash(String rawPassword);

    boolean verify(String rawPassword, PasswordHash hashedPassword);

    boolean isWeak(String rawPassword);

    int getMinLength();

    default ValidationResult validate(String rawPassword) {
        if (rawPassword == null || rawPassword.isEmpty()) {
            return ValidationResult.invalid("Password cannot be empty");
        }
        if (rawPassword.length() < getMinLength()) {
            return ValidationResult.invalid("Password must be at least " + getMinLength() + " characters");
        }
        if (isWeak(rawPassword)) {
            return ValidationResult.invalid("Password is too weak. Use a mix of uppercase, lowercase, numbers, and symbols");
        }
        return ValidationResult.createValid();
    }

    record ValidationResult(boolean valid, String message) {
        public static ValidationResult createValid() {
            return new ValidationResult(true, null);
        }

        public static ValidationResult invalid(String message) {
            return new ValidationResult(false, message);
        }
    }
}