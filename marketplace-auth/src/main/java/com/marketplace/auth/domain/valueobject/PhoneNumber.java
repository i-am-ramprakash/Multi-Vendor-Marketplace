package com.marketplace.auth.domain.valueobject;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhoneNumber {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[1-9]\\d{1,14}$");
    private static final int MAX_LENGTH = 20;

    private String value;

    public PhoneNumber(String value) {
        if (value == null || value.trim().isEmpty()) {
            this.value = null;
            return;
        }
        String cleaned = value.trim().replaceAll("[\\s\\-()]", "");
        if (cleaned.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Phone number cannot exceed " + MAX_LENGTH + " characters");
        }
        if (!PHONE_PATTERN.matcher(cleaned).matches()) {
            throw new IllegalArgumentException("Invalid phone number format. Use E.164 format (e.g., +1234567890)");
        }
        this.value = cleaned;
    }

    public static PhoneNumber of(String value) {
        return new PhoneNumber(value);
    }

    public static PhoneNumber empty() {
        return new PhoneNumber("");
    }

    public boolean isPresent() {
        return value != null;
    }

    @Override
    public String toString() {
        return value != null ? value : "";
    }
}