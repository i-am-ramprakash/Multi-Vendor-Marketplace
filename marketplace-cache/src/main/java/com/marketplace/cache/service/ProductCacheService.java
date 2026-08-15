package com.marketplace.cache.service;

import com.marketplace.cache.strategy.CacheKeyStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductCacheService {

    private final CacheService cacheService;

    public void cacheProductDetails(Long productId, Object product) {
        String key = CacheKeyStrategy.productDetails(productId);
        cacheService.put(key, product, 1, TimeUnit.HOURS);
        log.debug("Cached product details: productId={}", productId);
    }

    public <T> T getProductDetails(Long productId, Class<T> type) {
        String key = CacheKeyStrategy.productDetails(productId);
        return cacheService.get(key, type);
    }

    public void cacheProductVariants(Long productId, Object variants) {
        String key = CacheKeyStrategy.productVariants(productId);
        cacheService.put(key, variants, 1, TimeUnit.HOURS);
        log.debug("Cached product variants: productId={}", productId);
    }

    public <T> T getProductVariants(Long productId, Class<T> type) {
        String key = CacheKeyStrategy.productVariants(productId);
        return cacheService.get(key, type);
    }

    public void cacheProductImages(Long productId, Object images) {
        String key = CacheKeyStrategy.productImages(productId);
        cacheService.put(key, images, 1, TimeUnit.HOURS);
        log.debug("Cached product images: productId={}", productId);
    }

    public <T> T getProductImages(Long productId, Class<T> type) {
        String key = CacheKeyStrategy.productImages(productId);
        return cacheService.get(key, type);
    }

    public void cacheProductByVendor(Long vendorId, int page, Object products) {
        String key = CacheKeyStrategy.productByVendor(vendorId, page);
        cacheService.put(key, products, 1, TimeUnit.HOURS);
        log.debug("Cached products by vendor: vendorId={}, page={}", vendorId, page);
    }

    public <T> T getProductByVendor(Long vendorId, int page, Class<T> type) {
        String key = CacheKeyStrategy.productByVendor(vendorId, page);
        return cacheService.get(key, type);
    }

    public void cacheProductByCategory(Long categoryId, int page, Object products) {
        String key = CacheKeyStrategy.productByCategory(categoryId, page);
        cacheService.put(key, products, 1, TimeUnit.HOURS);
        log.debug("Cached products by category: categoryId={}, page={}", categoryId, page);
    }

    public <T> T getProductByCategory(Long categoryId, int page, Class<T> type) {
        String key = CacheKeyStrategy.productByCategory(categoryId, page);
        return cacheService.get(key, type);
    }

    public void cacheFeaturedProducts(Object products) {
        String key = CacheKeyStrategy.PRODUCT_FEATURED;
        cacheService.put(key, products, 30, TimeUnit.MINUTES);
        log.debug("Cached featured products");
    }

    public <T> T getFeaturedProducts(Class<T> type) {
        String key = CacheKeyStrategy.PRODUCT_FEATURED;
        return cacheService.get(key, type);
    }

    public void cachePopularProducts(Object products) {
        String key = CacheKeyStrategy.PRODUCT_POPULAR;
        cacheService.put(key, products, 30, TimeUnit.MINUTES);
        log.debug("Cached popular products");
    }

    public <T> T getPopularProducts(Class<T> type) {
        String key = CacheKeyStrategy.PRODUCT_POPULAR;
        return cacheService.get(key, type);
    }

    public void evictProduct(Long productId) {
        cacheService.deleteByPattern(CacheKeyStrategy.PRODUCT_PREFIX + ":" + productId + "*");
        log.debug("Evicted product cache: productId={}", productId);
    }

    public void evictProductsByVendor(Long vendorId) {
        cacheService.deleteByPattern(CacheKeyStrategy.PRODUCT_BY_VENDOR + ":" + vendorId + "*");
        log.debug("Evicted products by vendor cache: vendorId={}", vendorId);
    }

    public void evictProductsByCategory(Long categoryId) {
        cacheService.deleteByPattern(CacheKeyStrategy.PRODUCT_BY_CATEGORY + ":" + categoryId + "*");
        log.debug("Evicted products by category cache: categoryId={}", categoryId);
    }

    public void evictProductCache() {
        cacheService.deleteByPattern(CacheKeyStrategy.PRODUCT_PREFIX + ":*");
        log.debug("Evicted all product caches");
    }
}