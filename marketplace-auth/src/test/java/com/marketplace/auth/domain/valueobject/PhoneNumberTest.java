package com.marketplace.auth.domain.valueobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class PhoneNumberTest {

    @Test
    void of_WithValidE164Number_ShouldCreatePhoneNumber() {
        PhoneNumber phone = PhoneNumber.of("+1234567890");
        
        assertThat(phone.getValue()).isEqualTo("+1234567890");
        assertThat(phone.isPresent()).isTrue();
    }

    @Test
    void of_WithNumberWithSpacesAndDashes_ShouldCleanAndCreate() {
        PhoneNumber phone = PhoneNumber.of("+1 (234) 567-890");
        
        assertThat(phone.getValue()).isEqualTo("+1234567890");
        assertThat(phone.isPresent()).isTrue();
    }

    @Test
    void of_WithNull_ShouldReturnEmpty() {
        PhoneNumber phone = PhoneNumber.of(null);
        
        assertThat(phone.getValue()).isNull();
        assertThat(phone.isPresent()).isFalse();
    }

    @Test
    void of_WithEmptyString_ShouldReturnEmpty() {
        PhoneNumber phone = PhoneNumber.of("");
        
        assertThat(phone.getValue()).isNull();
        assertThat(phone.isPresent()).isFalse();
    }

    @Test
    void of_WithWhitespaceOnly_ShouldReturnEmpty() {
        PhoneNumber phone = PhoneNumber.of("   ");
        
        assertThat(phone.getValue()).isNull();
        assertThat(phone.isPresent()).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "+1234567890",
        "+442071234567",
        "+61234567890",
        "+81312345678",
        "+491234567890"
    })
    void of_WithVariousValidNumbers_ShouldCreatePhoneNumber(String validNumber) {
        PhoneNumber phone = PhoneNumber.of(validNumber);
        
        assertThat(phone.getValue()).isEqualTo(validNumber);
        assertThat(phone.isPresent()).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "1234567890",
        "+0123456789",
        "+123",
        "+1234567890123456",
        "abcdefghij",
        "+1-234-567-8900 ext 123"
    })
    void of_WithInvalidNumbers_ShouldThrowException(String invalidNumber) {
        assertThatThrownBy(() -> PhoneNumber.of(invalidNumber))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Invalid phone number format");
    }

    @Test
    void of_WithNumberTooLong_ShouldThrowException() {
        String longNumber = "+1" + "2".repeat(15);
        
        assertThatThrownBy(() -> PhoneNumber.of(longNumber))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("cannot exceed 20 characters");
    }

    @Test
    void empty_ShouldReturnEmptyPhoneNumber() {
        PhoneNumber phone = PhoneNumber.empty();
        
        assertThat(phone.getValue()).isNull();
        assertThat(phone.isPresent()).isFalse();
    }

    @Test
    void equals_WithSameNumber_ShouldBeEqual() {
        PhoneNumber phone1 = PhoneNumber.of("+1234567890");
        PhoneNumber phone2 = PhoneNumber.of("+1234567890");
        
        assertThat(phone1).isEqualTo(phone2);
        assertThat(phone1.hashCode()).isEqualTo(phone2.hashCode());
    }

    @Test
    void equals_WithDifferentNumber_ShouldNotBeEqual() {
        PhoneNumber phone1 = PhoneNumber.of("+1234567890");
        PhoneNumber phone2 = PhoneNumber.of("+0987654321");
        
        assertThat(phone1).isNotEqualTo(phone2);
    }

    @Test
    void equals_TwoEmptyPhoneNumbers_ShouldBeEqual() {
        PhoneNumber phone1 = PhoneNumber.empty();
        PhoneNumber phone2 = PhoneNumber.empty();
        
        assertThat(phone1).isEqualTo(phone2);
    }
}