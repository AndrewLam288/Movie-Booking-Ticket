package com.andrewlam.server.config;

import java.time.Duration;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.cache.autoconfigure.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        GenericJackson2JsonRedisSerializer jsonSerializer =
                new GenericJackson2JsonRedisSerializer()
                        .configure(objectMapper -> {
                            objectMapper.registerModule(new JavaTimeModule());
                            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                        });

        return RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string())
                )
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer)
                );
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer(
            RedisCacheConfiguration baseConfig
    ) {
        return (builder) -> builder
                .withCacheConfiguration(
                        "moviesList",
                        baseConfig.entryTtl(Duration.ofMinutes(30))
                )
                .withCacheConfiguration(
                        "movieDetail",
                        baseConfig.entryTtl(Duration.ofMinutes(30))
                )
                .withCacheConfiguration(
                        "cinemasList",
                        baseConfig.entryTtl(Duration.ofMinutes(30))
                )
                .withCacheConfiguration(
                        "cinemaDetail",
                        baseConfig.entryTtl(Duration.ofMinutes(30))
                )
                .withCacheConfiguration(
                        "showtimesByCinema",
                        baseConfig.entryTtl(Duration.ofMinutes(5))
                )
                .withCacheConfiguration(
                        "showtimesByMovie",
                        baseConfig.entryTtl(Duration.ofMinutes(5))
                )
                .withCacheConfiguration(
                        "showtimeDetail",
                        baseConfig.entryTtl(Duration.ofMinutes(5))
                );
    }
}