package com.xzcube.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author xzcube
 * @date 2021/6/8 14:47
 *
 * 配置RedisRTemplate，用来操作redis （设置redis数据的）
 */
@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        // 设置key的序列化方式  RedisSerializer.string()：返回一个能够序列化字符串的序列化方式
        redisTemplate.setKeySerializer(RedisSerializer.string());
        // 设置普通value序列化方式  RedisSerializer.json()：序列化成json
        redisTemplate.setValueSerializer(RedisSerializer.json());
        // 设置hash的key序列化方式
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        // 设置hash的value序列化方式
        redisTemplate.setHashValueSerializer(RedisSerializer.json());
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }
}
