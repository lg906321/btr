package com.github.btr.base.config;

import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis设置
 * Created by ryze on 2017/4/24.
 */
@Configuration
public class RedisConfiguration
{
	@Bean
	public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory)
	{
		val redisTemplate   = new StringRedisTemplate(factory);
		val redisSerializer = new StringRedisSerializer();
		redisTemplate.setKeySerializer(redisSerializer);
		redisTemplate.setValueSerializer(redisSerializer);
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}
}
