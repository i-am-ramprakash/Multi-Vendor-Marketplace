package com.marketplace.cache.service;

import com.marketplace.cache.strategy.CacheKeyStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class VendorCacheService {

    private final CacheService cacheService;

    public void cacheVendorProfile(Long vendorId, Object vendor) {
        String key = CacheKeyStrategy.vendorProfile(vendorId);
        cacheService.put(key, vendor, 2, TimeUnit.HOURS);
        log.debug("Cached vendor profile: vendorId={}", vendorId);
    }

    public <T> T getVendorProfile(Long vendorId, Class<T> type) {
        String key = CacheKeyStrategy.vendorProfile(vendorId);
        return cacheService.get(key, type);
    }

    public void cacheVendorList(Object vendors) {
        String key = CacheKeyStrategy.VENDOR_LIST;
        cacheService.put(key, vendors, 1, TimeUnit.HOURS);
        log.debug("Cached vendor list");
    }

    public <T> T getVendorList(Class<T> type) {
        String key = CacheKeyStrategy.VENDOR_LIST;
        return cacheService.get(key, type);
    }

    public void cacheVendorProducts(Long vendorId, int page, Object products) {
        String key = CacheKeyStrategy.vendorProducts(vendorId, page);
        cacheService.put(key, products, 1, TimeUnit.HOURS);
        log.debug("Cached vendor products: vendorId={}, page={}", vendorId, page);
    }

    public <T> T getVendorProducts(Long vendorId, int page, Class<T> type) {
        String key = CacheKeyStrategy.vendorProducts(vendorId, page);
        return cacheService.get(key, type);
    }

    public void cacheVendorStats(Long vendorId, Object stats) {
        String key = CacheKeyStrategy.vendorStats(vendorId);
        cacheService.put(key, stats, 2, TimeUnit.HOURS);
        log.debug("Cached vendor stats: vendorId={}", vendorId);
    }

    public <T> T getVendorStats(Long vendorId, Class<T> type) {
        String key = CacheKeyStrategy.vendorStats(vendorId);
        return cacheService.get(key, type);
    }

    public void cacheVendorAnalytics(Long vendorId, Object analytics) {
        String key = CacheKeyStrategy.vendorAnalytics(vendorId);
        cacheService.put(key, analytics, 30, TimeUnit.MINUTES);
        log.debug("Cached vendor analytics: vendorId={}", vendorId);
    }

    public <T> T getVendorAnalytics(Long vendorId, Class<T> type) {
        String key = CacheKeyStrategy.vendorAnalytics(vendorId);
        return cacheService.get(key, type);
    }

    public void evictVendor(Long vendorId) {
        cacheService.deleteByPattern(CacheKeyStrategy.VENDOR_PREFIX + ":" + vendorId + "*");
        log.debug("Evicted vendor cache: vendorId={}", vendorId);
    }

    public void evictVendorList() {
        cacheService.delete(CacheKeyStrategy.VENDOR_LIST);
        log.debug("Evicted vendor list cache");
    }

    public void evictVendorCache() {
        cacheService.deleteByPattern(CacheKeyStrategy.VENDOR_PREFIX + ":*");
        log.debug("Evicted all vendor caches");
    }
}