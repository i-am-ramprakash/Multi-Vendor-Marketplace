package com.marketplace.vendor.domain.valueobject;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreSlug {

    private static final Pattern SLUG_PATTERN = Pattern.compile("^[a-z0-9]+(?:-[a-z0-9]+)*$");
    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 200;

    private String value;

    public StoreSlug(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Store slug cannot be null or empty");
        }
        String trimmed = value.trim().toLowerCase();
        if (trimmed.length() < MIN_LENGTH) {
            throw new IllegalArgumentException("Store slug must be at least " + MIN_LENGTH + " characters");
        }
        if (trimmed.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Store slug cannot exceed " + MAX_LENGTH + " characters");
        }
        if (!SLUG_PATTERN.matcher(trimmed).matches()) {
            throw new IllegalArgumentException("Store slug can only contain lowercase letters, numbers, and hyphens");
        }
        this.value = trimmed;
    }

    public static StoreSlug of(String value) {
        return new StoreSlug(value);
    }

    public static StoreSlug fromStoreName(String storeName) {
        if (storeName == null || storeName.trim().isEmpty()) {
            throw new IllegalArgumentException("Store name cannot be null or empty");
        }
        String slug = storeName.trim()
            .toLowerCase()
            .replaceAll("[^a-z0-9\\s-]", "")
            .replaceAll("[\\s-]+", "-")
            .replaceAll("^-|-$", "");
        
        if (slug.length() < MIN_LENGTH) {
            slug = slug + "-store";
        }
        
        return new StoreSlug(slug);
    }

    public boolean isValid() {
        return value != null && SLUG_PATTERN.matcher(value).matches();
    }

    @Override
    public String toString() {
        return value;
    }
}