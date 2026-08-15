package com.marketplace.product.domain.valueobject;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductSlug {

    private static final Pattern SLUG_PATTERN = Pattern.compile("^[a-z0-9]+(?:-[a-z0-9]+)*$");
    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 255;

    private String value;

    public ProductSlug(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Product slug cannot be null or empty");
        }
        String trimmed = value.trim().toLowerCase();
        if (trimmed.length() < MIN_LENGTH) {
            throw new IllegalArgumentException("Product slug must be at least " + MIN_LENGTH + " characters");
        }
        if (trimmed.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Product slug cannot exceed " + MAX_LENGTH + " characters");
        }
        if (!SLUG_PATTERN.matcher(trimmed).matches()) {
            throw new IllegalArgumentException("Product slug can only contain lowercase letters, numbers, and hyphens");
        }
        this.value = trimmed;
    }

    public static ProductSlug of(String value) {
        return new ProductSlug(value);
    }

    public static ProductSlug fromProductName(String productName) {
        if (productName == null || productName.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        String slug = productName.trim()
            .toLowerCase()
            .replaceAll("[^a-z0-9\\s-]", "")
            .replaceAll("[\\s-]+", "-")
            .replaceAll("^-|-$", "");
        
        if (slug.length() < MIN_LENGTH) {
            slug = slug + "-product";
        }
        
        if (slug.length() > MAX_LENGTH) {
            slug = slug.substring(0, MAX_LENGTH);
            slug = slug.replaceAll("-$", "");
        }
        
        return new ProductSlug(slug);
    }

    public boolean isValid() {
        return value != null && SLUG_PATTERN.matcher(value).matches();
    }

    @Override
    public String toString() {
        return value;
    }
}