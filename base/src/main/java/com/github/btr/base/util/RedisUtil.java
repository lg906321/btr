package com.github.btr.base.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Redis
 * Created by ryze on 2017/4/30.
 */
@Slf4j
public class RedisUtil
{
	private RedisUtil() {}

	public static StringRedisTemplate stringRedisTemplate;

	public static String get(final String key)
	{
		try
		{
			return stringRedisTemplate.opsForValue().get(key);
		}
		catch (Exception e)
		{
			log.error("从Redis获取数据出错", e);
			return null;
		}
	}

	/**
	 * 获取JSONObject value
	 * @param key
	 * @return
	 */
	public static JSONObject getJSONObject(final String key)
	{
		return JSON.parseObject(get(key));
	}

	/**
	 * 获取指定类型 value
	 * @param key
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> T getData(final String key, final Class<T> clazz)
	{
		return JSON.parseObject(get(key), clazz);
	}

	/**
	 * 获取可选指定类型 value
	 * @param key
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> Optional<T> getOptional(final String key,final Class<T> clazz)
	{
			return Optional.ofNullable(getData(key,clazz));
	}

	public static JSONArray getJSONArray(final String key)
	{
		return JSON.parseArray(get(key));
	}

	/**
	 * 存入Redis => 默认无限钟
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean put(final String key, final String value)
	{
		return put(key, value, -1);
	}

	public static boolean put(final String key,final Object obj)
	{
		return put(key,JSON.toJSONString(obj),-1);
	}

	/**
	 * 存入Redis
	 * @param key
	 * @param value
	 * @param expire 时效
	 * @return
	 */
	public static boolean put(final String key, final String value, final long expire)
	{
		try
		{
			if(expire == -1)stringRedisTemplate.opsForValue().set(key,value);
			else stringRedisTemplate.opsForValue().set(key, value, expire,TimeUnit.MINUTES);
			return true;
		}
		catch (Exception e)
		{
			log.error("Redis存放数据出错", e);
			return false;
		}
	}

	public static boolean delete(String key)
	{
		try
		{
			stringRedisTemplate.delete(key);
			return true;
		}
		catch (Exception e)
		{
			log.error("Redis删除数据出错", e);
			return false;
		}
	}

	/**
	 * Key是否存在
	 * @param key
	 * @return
	 */
	public static boolean exists(final String key)
	{
		if(Objects.isNull(key))return false;
		return stringRedisTemplate.execute((RedisConnection connection) -> connection.exists(key.getBytes(StandardCharsets.UTF_8)));
	}

	/**
	 * 自增主键
	 * @param key
	 * @return
	 */
	public static long increment(final String key)
	{
		return stringRedisTemplate.opsForValue().increment(key, 1);
	}

	/**
	 * 延长Key时效
	 * @param key
	 * @param timeout 时长
	 * @param unit    单位
	 * @return
	 */
	public static Boolean expire(final String key, final long timeout, final TimeUnit unit)
	{
		return stringRedisTemplate.expire(key, timeout, unit);
	}

	/**
	 * 刷新缓存
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean refresh(final String key, final String value)
	{
		return exists(key) ? delete(key) : put(key, value);
	}

	/**
	 * 刷新数据库
	 */
	public static void flush()
	{
		stringRedisTemplate.execute((RedisCallback<String>) redisConnection ->
		{
			redisConnection.flushDb();
			return "success";
		});
	}
}
