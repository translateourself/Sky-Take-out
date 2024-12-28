package com.sky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author xuxunne
 * @description:
 * @since 2024/12/28 19:46
 */
@Configuration
@Slf4j
public class RedisConfiguration {

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory){

        log.info("start create redis template object");
        RedisTemplate redisTemplate = new RedisTemplate();
        // set redis connecting factory object
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //  set redis key serializer
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        return redisTemplate;

    }
}
