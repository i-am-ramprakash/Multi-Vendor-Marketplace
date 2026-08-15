package com.marketplace.auth.domain.service;

import com.marketplace.auth.domain.valueobject.PasswordHash;
import com.marketplace.auth.infrastructure.security.config.BCryptPasswordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class BCryptPasswordServiceTest {

    private PasswordService passwordService;

    @BeforeEach
    void setUp() {
        passwordService = new BCryptPasswordService();
    }

    @Test
    void hash_ShouldReturnHashedPassword() {
        String rawPassword = "SecurePass123!";
        
        PasswordHash hashed = passwordService.hash(rawPassword);
        
        assertThat(hashed).isNotNull();
        assertThat(hashed.getValue()).isNotEqualTo(rawPassword);
        assertThat(hashed.getValue()).startsWith("$2a$12$");
    }

    @Test
    void verify_WithCorrectPassword_ShouldReturnTrue() {
        String rawPassword = "SecurePass123!";
        PasswordHash hashed = passwordService.hash(rawPassword);
        
        boolean result = passwordService.verify(rawPassword, hashed);
        
        assertThat(result).isTrue();
    }

    @Test
    void verify_WithIncorrectPassword_ShouldReturnFalse() {
        String rawPassword = "SecurePass123!";
        String wrongPassword = "WrongPass123!";
        PasswordHash hashed = passwordService.hash(rawPassword);
        
        boolean result = passwordService.verify(wrongPassword, hashed);
        
        assertThat(result).isFalse();
    }

    @Test
    void verify_WithNullPassword_ShouldReturnFalse() {
        String rawPassword = "SecurePass123!";
        PasswordHash hashed = passwordService.hash(rawPassword);
        
        boolean result = passwordService.verify(null, hashed);
        
        assertThat(result).isFalse();
    }

    @Test
    void verify_WithNullHash_ShouldReturnFalse() {
        assertThat(passwordService.verify("password", null)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "short",
        "nouppercase123!",
        "NOLOWERCASE123!",
        "NoDigits!",
        "NoSpecialChars123",
        "alllowercase",
        "ALLUPPERCASE",
        "12345678"
    })
    void isWeak_WithWeakPasswords_ShouldReturnTrue(String weakPassword) {
        assertThat(passwordService.isWeak(weakPassword)).isTrue();
    }

    @Test
    void isWeak_WithStrongPassword_ShouldReturnFalse() {
        assertThat(passwordService.isWeak("StrongPass123!")).isFalse();
    }

    @Test
    void validate_WithValidPassword_ShouldReturnValid() {
        var result = passwordService.validate("StrongPass123!");
        
        assertThat(result.valid()).isTrue();
        assertThat(result.message()).isNull();
    }

    @Test
    void validate_WithNullPassword_ShouldReturnInvalid() {
        var result = passwordService.validate(null);
        
        assertThat(result.valid()).isFalse();
        assertThat(result.message()).isEqualTo("Password cannot be empty");
    }

    @Test
    void validate_WithEmptyPassword_ShouldReturnInvalid() {
        var result = passwordService.validate("");
        
        assertThat(result.valid()).isFalse();
        assertThat(result.message()).isEqualTo("Password cannot be empty");
    }

    @Test
    void validate_WithShortPassword_ShouldReturnInvalid() {
        var result = passwordService.validate("Short1!");
        
        assertThat(result.valid()).isFalse();
        assertThat(result.message()).contains("at least 8 characters");
    }

    @Test
    void validate_WithWeakPassword_ShouldReturnInvalid() {
        var result = passwordService.validate("weakpassword");
        
        assertThat(result.valid()).isFalse();
        assertThat(result.message()).contains("too weak");
    }

    @Test
    void getMinLength_ShouldReturn8() {
        assertThat(passwordService.getMinLength()).isEqualTo(8);
    }
}