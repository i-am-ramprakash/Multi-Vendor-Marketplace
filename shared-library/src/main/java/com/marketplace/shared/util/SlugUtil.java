package com.marketplace.shared.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

public final class SlugUtil {

    private static final Pattern NON_ALPHANUMERIC = Pattern.compile("[^a-z0-9\\-]");
    private static final Pattern WHITESPACE = Pattern.compile("\\s+");
    private static final Pattern EDGE_DASHES = Pattern.compile("^-|-$");

    private SlugUtil() {
    }

    public static String toSlug(String input) {
        if (input == null || input.isBlank()) {
            return "";
        }

        String normalized = Normalizer.normalize(input.toLowerCase(Locale.ROOT), Normalizer.Form.NFD);
        String slug = WHITESPACE.matcher(normalized).replaceAll("-");
        slug = NON_ALPHANUMERIC.matcher(slug).replaceAll("");
        slug = EDGE_DASHES.matcher(slug).replaceAll("");

        return slug;
    }

    public static String generateUniqueSlug(String baseSlug) {
        String slug = toSlug(baseSlug);
        if (slug.isEmpty()) {
            slug = UUID.randomUUID().toString().substring(0, 8);
        }
        return slug + "-" + UUID.randomUUID().toString().substring(0, 8);
    }
}
