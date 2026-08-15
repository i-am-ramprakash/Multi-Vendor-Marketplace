package com.marketplace.auth.domain.valueobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class EmailTest {

    @Test
    void of_WithValidEmail_ShouldCreateEmail() {
        Email email = Email.of("test@example.com");
        
        assertThat(email.getValue()).isEqualTo("test@example.com");
    }

    @Test
    void of_WithUpperCaseEmail_ShouldNormalizeToLowerCase() {
        Email email = Email.of("TEST@EXAMPLE.COM");
        
        assertThat(email.getValue()).isEqualTo("test@example.com");
    }

    @Test
    void of_WithWhitespace_ShouldTrim() {
        Email email = Email.of("  test@example.com  ");
        
        assertThat(email.getValue()).isEqualTo("test@example.com");
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "test@example.com",
        "user.name@domain.com",
        "user+tag@example.co.uk",
        "user123@test-domain.org",
        "a@b.co"
    })
    void of_WithVariousValidEmails_ShouldCreateEmail(String validEmail) {
        Email email = Email.of(validEmail);
        
        assertThat(email.getValue()).isEqualTo(validEmail.toLowerCase());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "",
        "   ",
        "invalid",
        "@example.com",
        "test@",
        "test@.com",
        "test@example",
        "test@example.c",
        "test@example.toolongtld",
        "test @example.com",
        "test@exam ple.com",
        "test@@example.com",
        "test@example..com"
    })
    void of_WithInvalidEmails_ShouldThrowException(String invalidEmail) {
        assertThatThrownBy(() -> Email.of(invalidEmail))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Invalid email format");
    }

    @Test
    void of_WithNullEmail_ShouldThrowException() {
        assertThatThrownBy(() -> Email.of(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("cannot be null or empty");
    }

    @Test
    void of_WithEmailTooLong_ShouldThrowException() {
        String longEmail = "a".repeat(250) + "@example.com";
        
        assertThatThrownBy(() -> Email.of(longEmail))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("cannot exceed 255 characters");
    }

    @Test
    void equals_WithSameEmail_ShouldBeEqual() {
        Email email1 = Email.of("test@example.com");
        Email email2 = Email.of("test@example.com");
        
        assertThat(email1).isEqualTo(email2);
        assertThat(email1.hashCode()).isEqualTo(email2.hashCode());
    }

    @Test
    void equals_WithDifferentEmail_ShouldNotBeEqual() {
        Email email1 = Email.of("test1@example.com");
        Email email2 = Email.of("test2@example.com");
        
        assertThat(email1).isNotEqualTo(email2);
    }

    @Test
    void toString_ShouldReturnEmailValue() {
        Email email = Email.of("test@example.com");
        
        assertThat(email.toString()).isEqualTo("test@example.com");
    }
}