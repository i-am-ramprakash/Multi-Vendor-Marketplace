package com.marketplace.product.domain.valueobject;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SKU {

    private static final Pattern SKU_PATTERN = Pattern.compile("^[A-Z0-9\\-]{3,50}$");
    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 50;

    private String value;

    public SKU(String value) {
        if (value == null || value.trim().isEmpty()) {
            this.value = null;
            return;
        }
        String trimmed = value.trim().toUpperCase();
        if (trimmed.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("SKU cannot exceed " + MAX_LENGTH + " characters");
        }
        if (!SKU_PATTERN.matcher(trimmed).matches()) {
            throw new IllegalArgumentException("SKU can only contain uppercase letters, numbers, and hyphens");
        }
        this.value = trimmed;
    }

    public static SKU of(String value) {
        return new SKU(value);
    }

    public static SKU generate(String prefix, Long productId) {
        if (prefix == null || prefix.trim().isEmpty()) {
            prefix = "PRD";
        }
        String formattedPrefix = prefix.trim().toUpperCase().replaceAll("[^A-Z]", "");
        if (formattedPrefix.isEmpty()) {
            formattedPrefix = "PRD";
        }
        return new SKU(formattedPrefix + "-" + String.format("%06d", productId));
    }

    public boolean isPresent() {
        return value != null && !value.isEmpty();
    }

    @Override
    public String toString() {
        return value != null ? value : "";
    }
}