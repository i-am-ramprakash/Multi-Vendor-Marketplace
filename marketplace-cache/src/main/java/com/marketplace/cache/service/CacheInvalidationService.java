package com.marketplace.cache.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CacheInvalidationService {

    private final ProductCacheService productCacheService;
    private final CategoryCacheService categoryCacheService;
    private final VendorCacheService vendorCacheService;
    private final SearchCacheService searchCacheService;
    private final CacheService cacheService;

    @KafkaListener(topics = "product-events", groupId = "cache-invalidation")
    public void handleProductEvent(String event) {
        log.info("Received product event for cache invalidation: {}", event);
        if (event.contains("PRODUCT_CREATED") || event.contains("PRODUCT_UPDATED")) {
            String productId = extractId(event, "productId");
            if (productId != null) {
                productCacheService.evictProduct(Long.parseLong(productId));
            }
        } else if (event.contains("PRODUCT_DELETED")) {
            String productId = extractId(event, "productId");
            if (productId != null) {
                productCacheService.evictProduct(Long.parseLong(productId));
            }
        }
    }

    @KafkaListener(topics = "category-events", groupId = "cache-invalidation")
    public void handleCategoryEvent(String event) {
        log.info("Received category event for cache invalidation: {}", event);
        if (event.contains("CATEGORY_CREATED") || event.contains("CATEGORY_UPDATED")) {
            String categoryId = extractId(event, "categoryId");
            if (categoryId != null) {
                categoryCacheService.evictCategory(Long.parseLong(categoryId));
            }
        } else if (event.contains("CATEGORY_DELETED")) {
            String categoryId = extractId(event, "categoryId");
            if (categoryId != null) {
                categoryCacheService.evictCategory(Long.parseLong(categoryId));
            }
        }
        categoryCacheService.evictCategoryList();
    }

    @KafkaListener(topics = "vendor-events", groupId = "cache-invalidation")
    public void handleVendorEvent(String event) {
        log.info("Received vendor event for cache invalidation: {}", event);
        if (event.contains("VENDOR_UPDATED") || event.contains("VENDOR_APPROVED")) {
            String vendorId = extractId(event, "vendorId");
            if (vendorId != null) {
                vendorCacheService.evictVendor(Long.parseLong(vendorId));
            }
        }
        vendorCacheService.evictVendorList();
    }

    @KafkaListener(topics = "search-events", groupId = "cache-invalidation")
    public void handleSearchEvent(String event) {
        log.info("Received search event for cache invalidation: {}", event);
        if (event.contains("INDEX_UPDATED")) {
            searchCacheService.evictSearchCache();
        }
    }

    public void invalidateProductCache(Long productId) {
        productCacheService.evictProduct(productId);
        searchCacheService.evictSearchCache();
        log.info("Invalidated product cache: productId={}", productId);
    }

    public void invalidateCategoryCache(Long categoryId) {
        categoryCacheService.evictCategory(categoryId);
        categoryCacheService.evictCategoryList();
        searchCacheService.evictSearchCache();
        log.info("Invalidated category cache: categoryId={}", categoryId);
    }

    public void invalidateVendorCache(Long vendorId) {
        vendorCacheService.evictVendor(vendorId);
        vendorCacheService.evictVendorList();
        log.info("Invalidated vendor cache: vendorId={}", vendorId);
    }

    public void invalidateAllCache() {
        productCacheService.evictProductCache();
        categoryCacheService.evictCategoryCache();
        vendorCacheService.evictVendorCache();
        searchCacheService.evictSearchCache();
        cacheService.clearCache();
        log.info("Invalidated all caches");
    }

    private String extractId(String event, String fieldName) {
        int startIndex = event.indexOf(fieldName + "=");
        if (startIndex == -1) {
            return null;
        }
        startIndex += fieldName.length() + 1;
        int endIndex = event.indexOf(",", startIndex);
        if (endIndex == -1) {
            endIndex = event.length();
        }
        return event.substring(startIndex, endIndex).trim();
    }
}