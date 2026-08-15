package com.marketplace.cache.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Slf4j
public class CacheServiceImpl implements CacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private final AtomicLong cacheHits = new AtomicLong(0);
    private final AtomicLong cacheMisses = new AtomicLong(0);

    public CacheServiceImpl(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public <T> void put(String key, T value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            log.debug("Cache PUT: key={}", key);
        } catch (Exception e) {
            log.error("Cache PUT error: key={}, error={}", key, e.getMessage());
        }
    }

    @Override
    public <T> void put(String key, T value, long ttl, TimeUnit unit) {
        try {
            redisTemplate.opsForValue().set(key, value, ttl, unit);
            log.debug("Cache PUT: key={}, ttl={} {}", key, ttl, unit);
        } catch (Exception e) {
            log.error("Cache PUT error: key={}, error={}", key, e.getMessage());
        }
    }

    @Override
    public <T> void put(String key, T value, Duration ttl) {
        try {
            redisTemplate.opsForValue().set(key, value, ttl);
            log.debug("Cache PUT: key={}, ttl={}", key, ttl);
        } catch (Exception e) {
            log.error("Cache PUT error: key={}, error={}", key, e.getMessage());
        }
    }

    @Override
    public <T> T get(String key, Class<T> type) {
        try {
            Object value = redisTemplate.opsForValue().get(key);
            if (value != null) {
                cacheHits.incrementAndGet();
                log.debug("Cache HIT: key={}", key);
                return type.cast(value);
            }
            cacheMisses.incrementAndGet();
            log.debug("Cache MISS: key={}", key);
            return null;
        } catch (Exception e) {
            log.error("Cache GET error: key={}, error={}", key, e.getMessage());
            cacheMisses.incrementAndGet();
            return null;
        }
    }

    @Override
    public Object get(String key) {
        try {
            Object value = redisTemplate.opsForValue().get(key);
            if (value != null) {
                cacheHits.incrementAndGet();
                log.debug("Cache HIT: key={}", key);
            } else {
                cacheMisses.incrementAndGet();
                log.debug("Cache MISS: key={}", key);
            }
            return value;
        } catch (Exception e) {
            log.error("Cache GET error: key={}, error={}", key, e.getMessage());
            cacheMisses.incrementAndGet();
            return null;
        }
    }

    @Override
    public boolean exists(String key) {
        try {
            Boolean exists = redisTemplate.hasKey(key);
            return Boolean.TRUE.equals(exists);
        } catch (Exception e) {
            log.error("Cache EXISTS error: key={}, error={}", key, e.getMessage());
            return false;
        }
    }

    @Override
    public void delete(String key) {
        try {
            redisTemplate.delete(key);
            log.debug("Cache DELETE: key={}", key);
        } catch (Exception e) {
            log.error("Cache DELETE error: key={}, error={}", key, e.getMessage());
        }
    }

    @Override
    public void delete(Collection<String> keys) {
        try {
            redisTemplate.delete(keys);
            log.debug("Cache DELETE: keys={}", keys.size());
        } catch (Exception e) {
            log.error("Cache DELETE error: keys={}, error={}", keys.size(), e.getMessage());
        }
    }

    @Override
    public long increment(String key, long delta) {
        try {
            Long result = redisTemplate.opsForValue().increment(key, delta);
            return result != null ? result : 0;
        } catch (Exception e) {
            log.error("Cache INCREMENT error: key={}, error={}", key, e.getMessage());
            return 0;
        }
    }

    @Override
    public long increment(String key) {
        return increment(key, 1);
    }

    @Override
    public <T> void putHash(String key, String field, T value) {
        try {
            redisTemplate.opsForHash().put(key, field, value);
            log.debug("Cache PUT_HASH: key={}, field={}", key, field);
        } catch (Exception e) {
            log.error("Cache PUT_HASH error: key={}, field={}, error={}", key, field, e.getMessage());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getHash(String key, String field, Class<T> type) {
        try {
            Object value = redisTemplate.opsForHash().get(key, field);
            if (value != null) {
                cacheHits.incrementAndGet();
                return type.cast(value);
            }
            cacheMisses.incrementAndGet();
            return null;
        } catch (Exception e) {
            log.error("Cache GET_HASH error: key={}, field={}, error={}", key, field, e.getMessage());
            return null;
        }
    }

    @Override
    public Map<Object, Object> getHashAll(String key) {
        try {
            return redisTemplate.opsForHash().entries(key);
        } catch (Exception e) {
            log.error("Cache GET_HASH_ALL error: key={}, error={}", key, e.getMessage());
            return Map.of();
        }
    }

    @Override
    public void deleteHash(String key, String field) {
        try {
            redisTemplate.opsForHash().delete(key, field);
            log.debug("Cache DELETE_HASH: key={}, field={}", key, field);
        } catch (Exception e) {
            log.error("Cache DELETE_HASH error: key={}, field={}, error={}", key, field, e.getMessage());
        }
    }

    @Override
    public boolean hasHash(String key, String field) {
        try {
            return redisTemplate.opsForHash().hasKey(key, field);
        } catch (Exception e) {
            log.error("Cache HAS_HASH error: key={}, field={}, error={}", key, field, e.getMessage());
            return false;
        }
    }

    @Override
    public long incrementHash(String key, String field, long delta) {
        try {
            return redisTemplate.opsForHash().increment(key, field, delta);
        } catch (Exception e) {
            log.error("Cache INCREMENT_HASH error: key={}, field={}, error={}", key, field, e.getMessage());
            return 0;
        }
    }

    @Override
    public <T> void leftPush(String key, T value) {
        try {
            redisTemplate.opsForList().leftPush(key, value);
        } catch (Exception e) {
            log.error("Cache LEFT_PUSH error: key={}, error={}", key, e.getMessage());
        }
    }

    @Override
    public <T> void rightPush(String key, T value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
        } catch (Exception e) {
            log.error("Cache RIGHT_PUSH error: key={}, error={}", key, e.getMessage());
        }
    }

    @Override
    public <T> T leftPop(String key, Class<T> type) {
        try {
            Object value = redisTemplate.opsForList().leftPop(key);
            return value != null ? type.cast(value) : null;
        } catch (Exception e) {
            log.error("Cache LEFT_POP error: key={}, error={}", key, e.getMessage());
            return null;
        }
    }

    @Override
    public <T> T rightPop(String key, Class<T> type) {
        try {
            Object value = redisTemplate.opsForList().rightPop(key);
            return value != null ? type.cast(value) : null;
        } catch (Exception e) {
            log.error("Cache RIGHT_POP error: key={}, error={}", key, e.getMessage());
            return null;
        }
    }

    @Override
    public long listSize(String key) {
        try {
            Long size = redisTemplate.opsForList().size(key);
            return size != null ? size : 0;
        } catch (Exception e) {
            log.error("Cache LIST_SIZE error: key={}, error={}", key, e.getMessage());
            return 0;
        }
    }

    @Override
    @SafeVarargs
    public final <T> void addSet(String key, T... values) {
        try {
            redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            log.error("Cache ADD_SET error: key={}, error={}", key, e.getMessage());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Set<T> getSet(String key, Class<T> type) {
        try {
            return (Set<T>) redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error("Cache GET_SET error: key={}, error={}", key, e.getMessage());
            return Set.of();
        }
    }

    @Override
    public boolean isSetMember(String key, Object value) {
        try {
            return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, value));
        } catch (Exception e) {
            log.error("Cache IS_SET_MEMBER error: key={}, error={}", key, e.getMessage());
            return false;
        }
    }

    @Override
    public long setSize(String key) {
        try {
            Long size = redisTemplate.opsForSet().size(key);
            return size != null ? size : 0;
        } catch (Exception e) {
            log.error("Cache SET_SIZE error: key={}, error={}", key, e.getMessage());
            return 0;
        }
    }

    @Override
    public Set<String> keys(String pattern) {
        try {
            return redisTemplate.keys(pattern);
        } catch (Exception e) {
            log.error("Cache KEYS error: pattern={}, error={}", pattern, e.getMessage());
            return Set.of();
        }
    }

    @Override
    public void deleteByPattern(String pattern) {
        try {
            Set<String> keys = keys(pattern);
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
                log.debug("Cache DELETE_BY_PATTERN: pattern={}, deleted={}", pattern, keys.size());
            }
        } catch (Exception e) {
            log.error("Cache DELETE_BY_PATTERN error: pattern={}, error={}", pattern, e.getMessage());
        }
    }

    @Override
    public boolean expire(String key, long ttl, TimeUnit unit) {
        try {
            return Boolean.TRUE.equals(redisTemplate.expire(key, ttl, unit));
        } catch (Exception e) {
            log.error("Cache EXPIRE error: key={}, error={}", key, e.getMessage());
            return false;
        }
    }

    @Override
    public long getTtl(String key, TimeUnit unit) {
        try {
            Long ttl = redisTemplate.getExpire(key, unit);
            return ttl != null ? ttl : -1;
        } catch (Exception e) {
            log.error("Cache GET_TTL error: key={}, error={}", key, e.getMessage());
            return -1;
        }
    }

    @Override
    public long getCacheHits() {
        return cacheHits.get();
    }

    @Override
    public long getCacheMisses() {
        return cacheMisses.get();
    }

    @Override
    public double getCacheHitRatio() {
        long total = cacheHits.get() + cacheMisses.get();
        return total > 0 ? (double) cacheHits.get() / total * 100 : 0;
    }

    @Override
    public void clearCache() {
        try {
            Set<String> keys = keys("marketplace:*");
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
                log.info("Cache CLEARED: {} keys deleted", keys.size());
            }
        } catch (Exception e) {
            log.error("Cache CLEAR error: {}", e.getMessage());
        }
    }

    @Override
    public void clearCache(String cacheName) {
        try {
            Set<String> keys = keys("marketplace:" + cacheName + ":*");
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
                log.info("Cache CLEARED: cacheName={}, {} keys deleted", cacheName, keys.size());
            }
        } catch (Exception e) {
            log.error("Cache CLEAR error: cacheName={}, {}", cacheName, e.getMessage());
        }
    }
}