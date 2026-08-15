package com.marketplace.cache.service;

import com.marketplace.cache.strategy.CacheKeyStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchCacheService {

    private final CacheService cacheService;

    public void cacheSearchResults(String query, int page, int size, Object results) {
        String key = CacheKeyStrategy.searchResults(query, page, size);
        cacheService.put(key, results, 15, TimeUnit.MINUTES);
        log.debug("Cached search results: query={}, page={}, size={}", query, page, size);
    }

    public <T> T getSearchResults(String query, int page, int size, Class<T> type) {
        String key = CacheKeyStrategy.searchResults(query, page, size);
        return cacheService.get(key, type);
    }

    public void cacheSearchSuggestions(String query, Object suggestions) {
        String key = CacheKeyStrategy.searchSuggestions(query);
        cacheService.put(key, suggestions, 30, TimeUnit.MINUTES);
        log.debug("Cached search suggestions: query={}", query);
    }

    public <T> T getSearchSuggestions(String query, Class<T> type) {
        String key = CacheKeyStrategy.searchSuggestions(query);
        return cacheService.get(key, type);
    }

    public void cacheSearchAutocomplete(String prefix, Object autocomplete) {
        String key = CacheKeyStrategy.searchAutocomplete(prefix);
        cacheService.put(key, autocomplete, 30, TimeUnit.MINUTES);
        log.debug("Cached search autocomplete: prefix={}", prefix);
    }

    public <T> T getSearchAutocomplete(String prefix, Class<T> type) {
        String key = CacheKeyStrategy.searchAutocomplete(prefix);
        return cacheService.get(key, type);
    }

    public void cacheSearchFilters(String query, Object filters) {
        String key = CacheKeyStrategy.SEARCH_FILTERS + ":" + CacheKeyStrategy.searchResults(query, 0, 10);
        cacheService.put(key, filters, 30, TimeUnit.MINUTES);
        log.debug("Cached search filters: query={}", query);
    }

    public <T> T getSearchFilters(String query, Class<T> type) {
        String key = CacheKeyStrategy.SEARCH_FILTERS + ":" + CacheKeyStrategy.searchResults(query, 0, 10);
        return cacheService.get(key, type);
    }

    public void evictSearchResults(String query) {
        cacheService.deleteByPattern(CacheKeyStrategy.SEARCH_RESULTS + ":*" + query.toLowerCase() + "*");
        log.debug("Evicted search results cache: query={}", query);
    }

    public void evictSearchCache() {
        cacheService.deleteByPattern(CacheKeyStrategy.SEARCH_PREFIX + ":*");
        log.debug("Evicted all search caches");
    }
}