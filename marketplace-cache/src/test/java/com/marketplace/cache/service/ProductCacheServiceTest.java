package com.marketplace.cache.service;

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

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class ProductCacheServiceTest {

    @Container
    static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:7-alpine"))
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
    }

    @Autowired
    private ProductCacheService productCacheService;

    @Autowired
    private CacheService cacheService;

    @BeforeEach
    void setUp() {
        cacheService.clearCache();
    }

    @Test
    void shouldCacheProductDetails() {
        // Given
        Map<String, Object> product = Map.of(
                "id", 1L,
                "name", "Test Product",
                "price", 99.99
        );

        // Cache
        productCacheService.cacheProductDetails(1L, product);

        // Retrieve
        Map<String, Object> cached = productCacheService.getProductDetails(1L, Map.class);
        assertThat(cached).isNotNull();
        assertThat(cached.get("name")).isEqualTo("Test Product");
    }

    @Test
    void shouldCacheProductByVendor() {
        // Given
        var products = java.util.List.of(Map.of("id", 1L, "name", "Product 1"));

        // Cache
        productCacheService.cacheProductByVendor(10L, 0, products);

        // Retrieve
        var cached = productCacheService.getProductByVendor(10L, 0, java.util.List.class);
        assertThat(cached).isNotNull();
        assertThat(cached).hasSize(1);
    }

    @Test
    void shouldEvictProductCache() {
        // Given
        productCacheService.cacheProductDetails(1L, Map.of("name", "Product"));

        // Evict
        productCacheService.evictProduct(1L);

        // Verify
        Map<String, Object> cached = productCacheService.getProductDetails(1L, Map.class);
        assertThat(cached).isNull();
    }
}