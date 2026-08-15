package com.marketplace.cache.service;

import com.marketplace.cache.strategy.CacheKeyStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryCacheService {

    private final CacheService cacheService;

    public void cacheCategoryList(Object categories) {
        String key = CacheKeyStrategy.CATEGORY_LIST;
        cacheService.put(key, categories, 6, TimeUnit.HOURS);
        log.debug("Cached category list");
    }

    public <T> T getCategoryList(Class<T> type) {
        String key = CacheKeyStrategy.CATEGORY_LIST;
        return cacheService.get(key, type);
    }

    public void cacheCategoryTree(Object categoryTree) {
        String key = CacheKeyStrategy.CATEGORY_TREE;
        cacheService.put(key, categoryTree, 6, TimeUnit.HOURS);
        log.debug("Cached category tree");
    }

    public <T> T getCategoryTree(Class<T> type) {
        String key = CacheKeyStrategy.CATEGORY_TREE;
        return cacheService.get(key, type);
    }

    public void cacheCategoryDetails(Long categoryId, Object category) {
        String key = CacheKeyStrategy.categoryDetails(categoryId);
        cacheService.put(key, category, 6, TimeUnit.HOURS);
        log.debug("Cached category details: categoryId={}", categoryId);
    }

    public <T> T getCategoryDetails(Long categoryId, Class<T> type) {
        String key = CacheKeyStrategy.categoryDetails(categoryId);
        return cacheService.get(key, type);
    }

    public void cacheCategoryProducts(Long categoryId, int page, Object products) {
        String key = CacheKeyStrategy.categoryProducts(categoryId, page);
        cacheService.put(key, products, 1, TimeUnit.HOURS);
        log.debug("Cached category products: categoryId={}, page={}", categoryId, page);
    }

    public <T> T getCategoryProducts(Long categoryId, int page, Class<T> type) {
        String key = CacheKeyStrategy.categoryProducts(categoryId, page);
        return cacheService.get(key, type);
    }

    public void cacheSubcategories(Long categoryId, Object subcategories) {
        String key = CacheKeyStrategy.categorySubcategories(categoryId);
        cacheService.put(key, subcategories, 6, TimeUnit.HOURS);
        log.debug("Cached subcategories: categoryId={}", categoryId);
    }

    public <T> T getSubcategories(Long categoryId, Class<T> type) {
        String key = CacheKeyStrategy.categorySubcategories(categoryId);
        return cacheService.get(key, type);
    }

    public void evictCategory(Long categoryId) {
        cacheService.deleteByPattern(CacheKeyStrategy.CATEGORY_PREFIX + ":" + categoryId + "*");
        log.debug("Evicted category cache: categoryId={}", categoryId);
    }

    public void evictCategoryList() {
        cacheService.delete(CacheKeyStrategy.CATEGORY_LIST);
        cacheService.delete(CacheKeyStrategy.CATEGORY_TREE);
        log.debug("Evicted category list and tree caches");
    }

    public void evictCategoryCache() {
        cacheService.deleteByPattern(CacheKeyStrategy.CATEGORY_PREFIX + ":*");
        log.debug("Evicted all category caches");
    }
}