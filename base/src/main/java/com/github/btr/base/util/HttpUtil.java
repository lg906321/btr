package com.github.btr.base.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

/**
 * 基础Http请求
 * Created by ryze on 2017/6/26.
 */
@Slf4j
public class HttpUtil
{
	public static RestTemplate restTemplate;

	public static JSONObject get(@NonNull final String uri, final String... queryString)
	{
		return get(uri, JSONObject.class, queryString);
	}

	/**
	 * Get请求
	 * @param uri         uri
	 * @param queryString queryString参数
	 * @return
	 */
	public static <T> T get(@NonNull final String uri, final Class<T> clazz, final String... queryString)
	{
		//规约
		val newUri = Arrays.stream(queryString).reduce((x, y) -> x + "&" + y)
										 //uri?queryString
										 .map(s -> uri + "?" + s)
										 .orElse(uri);
		return restTemplate.getForObject(newUri, clazz);
	}

	public static JSONObject post(final String uri, final Object obj)
	{
		return post(uri, obj, JSONObject.class);
	}

	/**
	 * Post请求
	 * @param uri  uri
	 * @param data body参数
	 * @return
	 */
	public static <T> T post(@NonNull final String uri, final Object data, final Class<T> clazz)
	{
		try
		{
			val u = new URI(uri);
			val entity = RequestEntity.post(u).contentType(MediaType.APPLICATION_JSON_UTF8).body(JSON.toJSONString(data));
			return restTemplate.postForObject(u, entity, clazz);
		}
		catch (URISyntaxException e)
		{
			log.error("Post请求出错", e);
			return null;
		}
	}
}
