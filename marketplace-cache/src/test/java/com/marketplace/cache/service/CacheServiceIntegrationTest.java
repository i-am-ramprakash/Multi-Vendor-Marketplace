package com.marketplace.cache.service;

import com.marketplace.cache.strategy.CacheKeyStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class CacheServiceIntegrationTest {

    @Container
    static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:7-alpine"))
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
    }

    @Autowired
    private CacheService cacheService;

    @BeforeEach
    void setUp() {
        cacheService.clearCache();
    }

    @Test
    void shouldPutAndGetValue() {
        // Put
        cacheService.put("test:key", "test-value", 1, TimeUnit.HOURS);

        // Get
        String value = cacheService.get("test:key", String.class);
        assertThat(value).isEqualTo("test-value");
    }

    @Test
    void shouldReturnNullForMissingKey() {
        // Get non-existent key
        String value = cacheService.get("nonexistent:key", String.class);
        assertThat(value).isNull();
    }

    @Test
    void shouldDeleteKey() {
        // Put
        cacheService.put("delete:key", "value");

        // Delete
        cacheService.delete("delete:key");

        // Verify
        assertThat(cacheService.exists("delete:key")).isFalse();
    }

    @Test
    void shouldIncrementValue() {
        // Increment
        long result1 = cacheService.increment("counter:key");
        long result2 = cacheService.increment("counter:key");

        assertThat(result1).isEqualTo(1);
        assertThat(result2).isEqualTo(2);
    }

    @Test
    void shouldPutAndGetHash() {
        // Put hash
        cacheService.putHash("hash:key", "field1", "value1");
        cacheService.putHash("hash:key", "field2", "value2");

        // Get hash
        String value1 = cacheService.getHash("hash:key", "field1", String.class);
        String value2 = cacheService.getHash("hash:key", "field2", String.class);

        assertThat(value1).isEqualTo("value1");
        assertThat(value2).isEqualTo("value2");
    }

    @Test
    void shouldTrackCacheStats() {
        // Miss
        cacheService.get("stats:miss", String.class);

        // Hit
        cacheService.put("stats:hit", "value");
        cacheService.get("stats:hit", String.class);

        assertThat(cacheService.getCacheMisses()).isGreaterThanOrEqualTo(1);
        assertThat(cacheService.getCacheHits()).isGreaterThanOrEqualTo(1);
        assertThat(cacheService.getCacheHitRatio()).isGreaterThan(0);
    }

    @Test
    void shouldDeleteByPattern() {
        // Put multiple keys
        cacheService.put("marketplace:product:1", "p1");
        cacheService.put("marketplace:product:2", "p2");
        cacheService.put("marketplace:category:1", "c1");

        // Delete by pattern
        cacheService.deleteByPattern("marketplace:product:*");

        // Verify
        assertThat(cacheService.exists("marketplace:product:1")).isFalse();
        assertThat(cacheService.exists("marketplace:product:2")).isFalse();
        assertThat(cacheService.exists("marketplace:category:1")).isTrue();
    }
}