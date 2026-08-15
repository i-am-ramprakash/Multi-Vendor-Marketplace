package com.marketplace.cache.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class RedisCacheConfig {

    @Value("${spring.data.redis.host:localhost}")
    private String redisHost;

    @Value("${spring.data.redis.port:6379}")
    private int redisPort;

    @Value("${spring.data.redis.password:}")
    private String redisPassword;

    @Value("${spring.data.redis.database:0}")
    private int redisDatabase;

    @Value("${spring.data.redis.lettuce.pool.max-active:16}")
    private int maxActive;

    @Value("${spring.data.redis.lettuce.pool.max-idle:8}")
    private int maxIdle;

    @Value("${spring.data.redis.lettuce.pool.min-idle:2}")
    private int minIdle;

    @Value("${spring.data.redis.timeout:5000ms}")
    private Duration timeout;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisHost);
        config.setPort(redisPort);
        config.setDatabase(redisDatabase);
        if (redisPassword != null && !redisPassword.isBlank()) {
            config.setPassword(redisPassword);
        }

        LettucePoolingClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
            .commandTimeout(Duration.ofSeconds(5))
            .poolConfig(createConnectionPool())
            .build();

        return new LettuceConnectionFactory(config, clientConfig);
    }

    private org.apache.commons.pool2.impl.GenericObjectPoolConfig<?> createConnectionPool() {
        org.apache.commons.pool2.impl.GenericObjectPoolConfig<?> poolConfig =
            new org.apache.commons.pool2.impl.GenericObjectPoolConfig<>();
        poolConfig.setMaxTotal(maxActive);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setMaxWait(Duration.ofMillis(3000));
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        return poolConfig;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        ObjectMapper objectMapper = createObjectMapper();

        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);
        StringRedisSerializer stringSerializer = new StringRedisSerializer();

        template.setKeySerializer(stringSerializer);
        template.setValueSerializer(jsonSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setHashValueSerializer(jsonSerializer);
        template.setEnableTransactionSupport(true);
        template.afterPropertiesSet();

        return template;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        ObjectMapper objectMapper = createObjectMapper();

        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);
        StringRedisSerializer stringSerializer = new StringRedisSerializer();

        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(30))
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(stringSerializer))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer))
            .disableCachingNullValues()
            .prefixCacheNameWith("marketplace:");

        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        // Product cache - 1 hour TTL
        cacheConfigurations.put("products", defaultConfig.entryTtl(Duration.ofHours(1)));
        cacheConfigurations.put("product:details", defaultConfig.entryTtl(Duration.ofHours(1)));
        cacheConfigurations.put("product:variants", defaultConfig.entryTtl(Duration.ofHours(1)));
        cacheConfigurations.put("product:images", defaultConfig.entryTtl(Duration.ofHours(2)));

        // Category cache - 6 hours TTL (rarely changes)
        cacheConfigurations.put("categories", defaultConfig.entryTtl(Duration.ofHours(6)));
        cacheConfigurations.put("category:tree", defaultConfig.entryTtl(Duration.ofHours(6)));
        cacheConfigurations.put("category:products", defaultConfig.entryTtl(Duration.ofMinutes(30)));

        // Vendor cache - 2 hours TTL
        cacheConfigurations.put("vendors", defaultConfig.entryTtl(Duration.ofHours(2)));
        cacheConfigurations.put("vendor:profile", defaultConfig.entryTtl(Duration.ofHours(2)));
        cacheConfigurations.put("vendor:products", defaultConfig.entryTtl(Duration.ofMinutes(30)));
        cacheConfigurations.put("vendor:stats", defaultConfig.entryTtl(Duration.ofHours(1)));

        // Search cache - 15 minutes TTL
        cacheConfigurations.put("search:results", defaultConfig.entryTtl(Duration.ofMinutes(15)));
        cacheConfigurations.put("search:suggestions", defaultConfig.entryTtl(Duration.ofMinutes(30)));
        cacheConfigurations.put("search:filters", defaultConfig.entryTtl(Duration.ofHours(1)));

        // URL shortener cache - 1 hour TTL
        cacheConfigurations.put("urls", defaultConfig.entryTtl(Duration.ofHours(1)));
        cacheConfigurations.put("url:short", defaultConfig.entryTtl(Duration.ofHours(1)));

        // Cart cache - 30 minutes TTL
        cacheConfigurations.put("cart", defaultConfig.entryTtl(Duration.ofMinutes(30)));
        cacheConfigurations.put("cart:items", defaultConfig.entryTtl(Duration.ofMinutes(30)));

        // Session cache - 24 hours TTL
        cacheConfigurations.put("sessions", defaultConfig.entryTtl(Duration.ofHours(24)));

        // Rate limiting - 1 hour TTL
        cacheConfigurations.put("ratelimit", defaultConfig.entryTtl(Duration.ofHours(1)));

        return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(defaultConfig)
            .withInitialCacheConfigurations(cacheConfigurations)
            .transactionAware()
            .build();
    }

    private ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(
            objectMapper.getPolymorphicTypeValidator(),
            ObjectMapper.DefaultTyping.NON_FINAL,
            JsonTypeInfo.As.PROPERTY
        );
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }
}