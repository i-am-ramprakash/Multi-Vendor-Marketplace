package com.marketplace.cache.strategy;

import org.springframework.stereotype.Component;

@Component
public class CacheKeyStrategy {

    private static final String PREFIX = "marketplace";
    private static final String SEPARATOR = ":";

    // Product Keys
    public static final String PRODUCT_PREFIX = PREFIX + SEPARATOR + "product";
    public static final String PRODUCT_DETAILS = PRODUCT_PREFIX + SEPARATOR + "details";
    public static final String PRODUCT_VARIANTS = PRODUCT_PREFIX + SEPARATOR + "variants";
    public static final String PRODUCT_IMAGES = PRODUCT_PREFIX + SEPARATOR + "images";
    public static final String PRODUCT_SEARCH = PREFIX + SEPARATOR + "search" + SEPARATOR + "products";
    public static final String PRODUCT_BY_VENDOR = PRODUCT_PREFIX + SEPARATOR + "vendor";
    public static final String PRODUCT_BY_CATEGORY = PRODUCT_PREFIX + SEPARATOR + "category";
    public static final String PRODUCT_FEATURED = PRODUCT_PREFIX + SEPARATOR + "featured";
    public static final String PRODUCT_POPULAR = PRODUCT_PREFIX + SEPARATOR + "popular";

    // Category Keys
    public static final String CATEGORY_PREFIX = PREFIX + SEPARATOR + "category";
    public static final String CATEGORY_LIST = CATEGORY_PREFIX + SEPARATOR + "list";
    public static final String CATEGORY_TREE = CATEGORY_PREFIX + SEPARATOR + "tree";
    public static final String CATEGORY_DETAILS = CATEGORY_PREFIX + SEPARATOR + "details";
    public static final String CATEGORY_PRODUCTS = CATEGORY_PREFIX + SEPARATOR + "products";
    public static final String CATEGORY_SUBCATEGORIES = CATEGORY_PREFIX + SEPARATOR + "subcategories";

    // Vendor Keys
    public static final String VENDOR_PREFIX = PREFIX + SEPARATOR + "vendor";
    public static final String VENDOR_PROFILE = VENDOR_PREFIX + SEPARATOR + "profile";
    public static final String VENDOR_LIST = VENDOR_PREFIX + SEPARATOR + "list";
    public static final String VENDOR_PRODUCTS = VENDOR_PREFIX + SEPARATOR + "products";
    public static final String VENDOR_STATS = VENDOR_PREFIX + SEPARATOR + "stats";
    public static final String VENDOR_ANALYTICS = VENDOR_PREFIX + SEPARATOR + "analytics";

    // Search Keys
    public static final String SEARCH_PREFIX = PREFIX + SEPARATOR + "search";
    public static final String SEARCH_RESULTS = SEARCH_PREFIX + SEPARATOR + "results";
    public static final String SEARCH_SUGGESTIONS = SEARCH_PREFIX + SEPARATOR + "suggestions";
    public static final String SEARCH_FILTERS = SEARCH_PREFIX + SEPARATOR + "filters";
    public static final String SEARCH_AUTOCOMPLETE = SEARCH_PREFIX + SEPARATOR + "autocomplete";

    // Cart Keys
    public static final String CART_PREFIX = PREFIX + SEPARATOR + "cart";
    public static final String CART_DETAILS = CART_PREFIX + SEPARATOR + "details";
    public static final String CART_ITEMS = CART_PREFIX + SEPARATOR + "items";
    public static final String CART_SUMMARY = CART_PREFIX + SEPARATOR + "summary";

    // URL Shortener Keys
    public static final String URL_PREFIX = PREFIX + SEPARATOR + "url";
    public static final String URL_SHORT = URL_PREFIX + SEPARATOR + "short";
    public static final String URL_CLICKS = URL_PREFIX + SEPARATOR + "clicks";

    // Session Keys
    public static final String SESSION_PREFIX = PREFIX + SEPARATOR + "session";
    public static final String SESSION_USER = SESSION_PREFIX + SEPARATOR + "user";
    public static final String SESSION_TOKEN = SESSION_PREFIX + SEPARATOR + "token";

    // Rate Limit Keys
    public static final String RATELIMIT_PREFIX = PREFIX + SEPARATOR + "ratelimit";
    public static final String RATELIMIT_API = RATELIMIT_PREFIX + SEPARATOR + "api";
    public static final String RATELIMIT_USER = RATELIMIT_PREFIX + SEPARATOR + "user";

    // Helper methods for dynamic keys
    public static String productDetails(Long productId) {
        return PRODUCT_DETAILS + SEPARATOR + productId;
    }

    public static String productVariants(Long productId) {
        return PRODUCT_VARIANTS + SEPARATOR + productId;
    }

    public static String productImages(Long productId) {
        return PRODUCT_IMAGES + SEPARATOR + productId;
    }

    public static String productByVendor(Long vendorId, int page) {
        return PRODUCT_BY_VENDOR + SEPARATOR + vendorId + SEPARATOR + "page" + SEPARATOR + page;
    }

    public static String productByCategory(Long categoryId, int page) {
        return PRODUCT_BY_CATEGORY + SEPARATOR + categoryId + SEPARATOR + "page" + SEPARATOR + page;
    }

    public static String categoryDetails(Long categoryId) {
        return CATEGORY_DETAILS + SEPARATOR + categoryId;
    }

    public static String categoryProducts(Long categoryId, int page) {
        return CATEGORY_PRODUCTS + SEPARATOR + categoryId + SEPARATOR + "page" + SEPARATOR + page;
    }

    public static String categorySubcategories(Long categoryId) {
        return CATEGORY_SUBCATEGORIES + SEPARATOR + categoryId;
    }

    public static String vendorProfile(Long vendorId) {
        return VENDOR_PROFILE + SEPARATOR + vendorId;
    }

    public static String vendorProducts(Long vendorId, int page) {
        return VENDOR_PRODUCTS + SEPARATOR + vendorId + SEPARATOR + "page" + SEPARATOR + page;
    }

    public static String vendorStats(Long vendorId) {
        return VENDOR_STATS + SEPARATOR + vendorId;
    }

    public static String vendorAnalytics(Long vendorId) {
        return VENDOR_ANALYTICS + SEPARATOR + vendorId;
    }

    public static String searchResults(String query, int page, int size) {
        return SEARCH_RESULTS + SEPARATOR + hashQuery(query) + SEPARATOR + "page" + SEPARATOR + page + SEPARATOR + "size" + SEPARATOR + size;
    }

    public static String searchSuggestions(String query) {
        return SEARCH_SUGGESTIONS + SEPARATOR + hashQuery(query);
    }

    public static String searchAutocomplete(String prefix) {
        return SEARCH_AUTOCOMPLETE + SEPARATOR + prefix.toLowerCase();
    }

    public static String cartDetails(Long userId) {
        return CART_DETAILS + SEPARATOR + userId;
    }

    public static String cartItems(Long userId) {
        return CART_ITEMS + SEPARATOR + userId;
    }

    public static String urlShort(String shortCode) {
        return URL_SHORT + SEPARATOR + shortCode;
    }

    public static String sessionUser(Long userId) {
        return SESSION_USER + SEPARATOR + userId;
    }

    public static String sessionToken(String token) {
        return SESSION_TOKEN + SEPARATOR + token;
    }

    public static String rateLimitApi(String clientId, String endpoint) {
        return RATELIMIT_API + SEPARATOR + clientId + SEPARATOR + endpoint;
    }

    public static String rateLimitUser(Long userId, String action) {
        return RATELIMIT_USER + SEPARATOR + userId + SEPARATOR + action;
    }

    private static String hashQuery(String query) {
        return String.valueOf(query.toLowerCase().hashCode());
    }
}