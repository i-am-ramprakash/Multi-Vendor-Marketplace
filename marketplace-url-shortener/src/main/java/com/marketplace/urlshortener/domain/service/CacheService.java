package com.marketplace.urlshortener.domain.service;

public interface CacheService {

    void put(String key, String value, long ttlSeconds);

    String get(String key);

    void delete(String key);

    boolean exists(String key);

    void putHash(String key, String field, String value);

    String getHash(String key, String field);

    void incrementHash(String key, String field);

    void setExpiration(String key, long ttlSeconds);
}