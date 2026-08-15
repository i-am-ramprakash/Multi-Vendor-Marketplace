package com.marketplace.cache.controller;

import com.marketplace.cache.service.CacheInvalidationService;
import com.marketplace.cache.service.CacheService;
import com.marketplace.cache.strategy.CacheKeyStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/v1/cache")
@RequiredArgsConstructor
@Slf4j
public class CacheMonitoringController {

    private final CacheService cacheService;
    private final CacheInvalidationService cacheInvalidationService;

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getCacheStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("cacheHits", cacheService.getCacheHits());
        stats.put("cacheMisses", cacheService.getCacheMisses());
        stats.put("cacheHitRatio", cacheService.getCacheHitRatio());
        stats.put("totalKeys", getAllKeys().getBody().size());
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/keys")
    public ResponseEntity<Set<String>> getAllKeys() {
        Set<String> keys = cacheService.keys("marketplace:*");
        return ResponseEntity.ok(keys);
    }

    @GetMapping("/keys/{pattern}")
    public ResponseEntity<Set<String>> getKeysByPattern(@PathVariable String pattern) {
        Set<String> keys = cacheService.keys("marketplace:" + pattern + ":*");
        return ResponseEntity.ok(keys);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<Object> getProductCache(@PathVariable Long productId) {
        String key = CacheKeyStrategy.productDetails(productId);
        return ResponseEntity.ok(cacheService.get(key));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Object> getCategoryCache(@PathVariable Long categoryId) {
        String key = CacheKeyStrategy.categoryDetails(categoryId);
        return ResponseEntity.ok(cacheService.get(key));
    }

    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<Object> getVendorCache(@PathVariable Long vendorId) {
        String key = CacheKeyStrategy.vendorProfile(vendorId);
        return ResponseEntity.ok(cacheService.get(key));
    }

    @DeleteMapping("/product/{productId}")
    public ResponseEntity<Void> evictProductCache(@PathVariable Long productId) {
        cacheInvalidationService.invalidateProductCache(productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/category/{categoryId}")
    public ResponseEntity<Void> evictCategoryCache(@PathVariable Long categoryId) {
        cacheInvalidationService.invalidateCategoryCache(categoryId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/vendor/{vendorId}")
    public ResponseEntity<Void> evictVendorCache(@PathVariable Long vendorId) {
        cacheInvalidationService.invalidateVendorCache(vendorId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> evictAllCache() {
        cacheInvalidationService.invalidateAllCache();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> getCacheHealth() {
        Map<String, Object> health = new HashMap<>();
        try {
            cacheService.exists("marketplace:health:check");
            health.put("status", "UP");
            health.put("redis", "Connected");
        } catch (Exception e) {
            health.put("status", "DOWN");
            health.put("redis", "Disconnected");
            health.put("error", e.getMessage());
        }
        return ResponseEntity.ok(health);
    }
}