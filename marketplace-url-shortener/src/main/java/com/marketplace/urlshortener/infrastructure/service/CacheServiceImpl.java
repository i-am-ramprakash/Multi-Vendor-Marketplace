package com.marketplace.urlshortener.infrastructure.service;

import com.marketplace.urlshortener.domain.service.CacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService {

    private final StringRedisTemplate redisTemplate;

    @Override
    public void put(String key, String value, long ttlSeconds) {
        try {
            redisTemplate.opsForValue().set(key, value, ttlSeconds, TimeUnit.SECONDS);
            log.debug("Cache put: key={}, ttl={}s", key, ttlSeconds);
        } catch (Exception e) {
            log.error("Failed to put cache: key={}, error={}", key, e.getMessage());
        }
    }

    @Override
    public String get(String key) {
        try {
            String value = redisTemplate.opsForValue().get(key);
            log.debug("Cache get: key={}, hit={}", key, value != null);
            return value;
        } catch (Exception e) {
            log.error("Failed to get cache: key={}, error={}", key, e.getMessage());
            return null;
        }
    }

    @Override
    public void delete(String key) {
        try {
            redisTemplate.delete(key);
            log.debug("Cache delete: key={}", key);
        } catch (Exception e) {
            log.error("Failed to delete cache: key={}, error={}", key, e.getMessage());
        }
    }

    @Override
    public boolean exists(String key) {
        try {
            Boolean exists = redisTemplate.hasKey(key);
            return Boolean.TRUE.equals(exists);
        } catch (Exception e) {
            log.error("Failed to check cache existence: key={}, error={}", key, e.getMessage());
            return false;
        }
    }

    @Override
    public void putHash(String key, String field, String value) {
        try {
            redisTemplate.opsForHash().put(key, field, value);
            log.debug("Cache putHash: key={}, field={}", key, field);
        } catch (Exception e) {
            log.error("Failed to put hash cache: key={}, field={}, error={}", key, field, e.getMessage());
        }
    }

    @Override
    public String getHash(String key, String field) {
        try {
            Object value = redisTemplate.opsForHash().get(key, field);
            log.debug("Cache getHash: key={}, field={}, hit={}", key, field, value != null);
            return value != null ? value.toString() : null;
        } catch (Exception e) {
            log.error("Failed to get hash cache: key={}, field={}, error={}", key, field, e.getMessage());
            return null;
        }
    }

    @Override
    public void incrementHash(String key, String field) {
        try {
            redisTemplate.opsForHash().increment(key, field, 1);
            log.debug("Cache incrementHash: key={}, field={}", key, field);
        } catch (Exception e) {
            log.error("Failed to increment hash cache: key={}, field={}, error={}", key, field, e.getMessage());
        }
    }

    @Override
    public void setExpiration(String key, long ttlSeconds) {
        try {
            redisTemplate.expire(key, ttlSeconds, TimeUnit.SECONDS);
            log.debug("Cache setExpiration: key={}, ttl={}s", key, ttlSeconds);
        } catch (Exception e) {
            log.error("Failed to set cache expiration: key={}, error={}", key, e.getMessage());
        }
    }
}