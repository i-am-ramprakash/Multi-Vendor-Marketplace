package com.marketplace.cache.service;

import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface CacheService {

    // Basic operations
    <T> void put(String key, T value);

    <T> void put(String key, T value, long ttl, TimeUnit unit);

    <T> void put(String key, T value, Duration ttl);

    <T> T get(String key, Class<T> type);

    Object get(String key);

    boolean exists(String key);

    void delete(String key);

    void delete(Collection<String> keys);

    long increment(String key, long delta);

    long increment(String key);

    // Hash operations
    <T> void putHash(String key, String field, T value);

    <T> T getHash(String key, String field, Class<T> type);

    Map<Object, Object> getHashAll(String key);

    void deleteHash(String key, String field);

    boolean hasHash(String key, String field);

    long incrementHash(String key, String field, long delta);

    // List operations
    <T> void leftPush(String key, T value);

    <T> void rightPush(String key, T value);

    <T> T leftPop(String key, Class<T> type);

    <T> T rightPop(String key, Class<T> type);

    long listSize(String key);

    // Set operations
    <T> void addSet(String key, T... values);

    <T> Set<T> getSet(String key, Class<T> type);

    boolean isSetMember(String key, Object value);

    long setSize(String key);

    // Pattern operations
    Set<String> keys(String pattern);

    void deleteByPattern(String pattern);

    // Expiration
    boolean expire(String key, long ttl, TimeUnit unit);

    long getTtl(String key, TimeUnit unit);

    // Cache statistics
    long getCacheHits();

    long getCacheMisses();

    double getCacheHitRatio();

    void clearCache();

    void clearCache(String cacheName);
}