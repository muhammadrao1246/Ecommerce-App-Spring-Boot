package com.muhammadrao1246.SpringDemoApp.configs;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.hash.ObjectHashMapper;
import org.springframework.data.redis.serializer.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;

// Here we can override or modify or add configurations for the app
@Configuration
// Spring AOP is going to handle it on its own.
// setting up default cache manager and in-memory store using concurrent-map in java
// or Other cache providers can be used Redis i.e. which implements the Cache Manager ApI contract of spring
@EnableCaching

public class AppConfig {

    // for spring security
    // Password Encoder global declaration
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // defining cacheManager
    @Bean
    CacheManager cacheManager(RedisConnectionFactory factory, ObjectMapper objectMapper){
//        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(factory).build();
        return new ConcurrentMapCacheManager();


//        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofSeconds(60))
//                .disableCachingNullValues()
//                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
//                ;
////
//        return RedisCacheManager.builder(factory)
//                .cacheDefaults(config)
//                .build();
    }
}
