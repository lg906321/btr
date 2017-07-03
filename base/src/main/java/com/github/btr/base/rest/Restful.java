package com.github.btr.base.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.btr.base.util.CommonUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Rest风格工具
 * Created by ryze on 2017/4/27.
 */
@Slf4j
public final class Restful
{
	public static String apiGateway;

	/**
	 * 资源转换
	 * @param r
	 * @param clazz
	 * @param <R>   资源类型
	 * @param <T>   实体类型
	 * @return
	 */
	public static <R extends Resource, T> T convertResource(R r, Class<T> clazz)
	{
		val json = JSON.toJSONString(r);
		return JSON.parseObject(json, clazz);
	}

	/**
	 * 给资源添加超文本驱动
	 * @param resource 资源
	 * @param rel      资源描述符
	 * @param href     资源uri 可填写相对或绝对地址 例如 /orders http://localhost:80/orders
	 * @return
	 */
	public static Resource addHyperDriven(final Resource resource, final String rel, final String href)
	{
		val sb = CommonUtil.getStringBuilder();
		val newHref = Optional.ofNullable(href)
											.map(uri -> uri.startsWith("http") ? uri :
																			sb.append(apiGateway).append(getUri()).append(href).toString())
											.orElseGet(() -> sb.append(apiGateway).append(getUri()).toString());

		resource.links.add(Link.builder().rel(rel).href(newHref).build());

		return resource;
	}

	/**
	 * CRUD资源创建
	 * @param resource 资源
	 * @param f        函数
	 * @return
	 */
	private static Resource crud(final Resource resource, final Function<Link, Resource> f)
	{
		return resource.getLink(Rel.SELF).map(f)
							 .orElseThrow(() -> new IllegalArgumentException("Link Not Found"));
	}

	/**
	 * 创建资源
	 * @param resource
	 * @return
	 */
	public static Resource create(final Resource resource)
	{
		return crud(resource, data -> addHyperDriven(resource, Rel.CREATE, data.getHref()));
	}

	/**
	 * 查看资源
	 * @param resource
	 * @return
	 */
	public static Resource retrieve(final Resource resource)
	{
		return crud(resource, data -> addHyperDriven(resource, Rel.RETRIEVE, data.getHref()));
	}

	/**
	 * 修改资源
	 * @param resource
	 * @return
	 */
	public static Resource update(final Resource resource)
	{
		return crud(resource, data -> addHyperDriven(resource, Rel.UPDATE, data.getHref()));
	}

	/**
	 * 删除资源
	 * @param resource
	 * @return
	 */
	public static Resource delete(final Resource resource)
	{
		return crud(resource, data -> addHyperDriven(resource, Rel.DELETE, data.getHref()));
	}

	/**
	 * UD资源
	 * @param resource
	 * @return
	 */
	public static Resource ud(final Resource resource)
	{
		return Optional.of(resource).map(Restful::update).map(Restful::delete).get();
	}

	/**
	 * RUD资源
	 * @param resource
	 * @return
	 */
	public static Resource rud(final Resource resource)
	{
		return Optional.of(resource).map(Restful::retrieve).map(Restful::ud).get();
	}

	/**
	 * CRUD资源
	 * @param resource
	 * @return
	 */
	public static Resource crud(final Resource resource)
	{
		return Optional.of(resource).map(Restful::create).map(Restful::rud).get();
	}

	/**
	 * 单个资源转换
	 * @param obj  实体类
	 * @param type 有值则代表转换的是集合资源
	 * @param <T>
	 * @return
	 */
	public static <T extends Resource> Optional<Resource> toResource(final T obj, final Boolean... type)
	{
		val data     = JSON.toJSONString(obj);
		val json     = JSON.parseObject(data);
		val resource = JSON.toJavaObject(json, obj.getClass());
		//所有资源都有对自身的资源描述
		val sb = CommonUtil.getStringBuilder();
		sb.append(apiGateway).append(getUri());
		val newResource = Arrays.stream(type)
													.map(t -> addHyperDriven(resource, Rel.SELF, sb.append("/").append(json.getString("id")).toString()))
													.findAny()
													.orElseGet(() -> addHyperDriven(resource, Rel.SELF, sb.toString()));

		return Optional.of(newResource);
	}

	/**
	 * 复数资源转换
	 * @param page      分页
	 * @param resources 资源list
	 * @param <T>
	 * @return
	 */
	public static <T extends Resource> Optional<JSONObject> toResources(final Page page, final List resources)
	{
		val resource   = new JSONObject();
		val currentUri = CommonUtil.getStringBuilder().append(apiGateway).append(getUri()).append("?").append(getQueryString());
		val number     = page.getNumber();
		val size       = page.getSize();

		//资源
		resource.put("data", resources);

		//超文本驱动
		val links = new ArrayList<Link>();
		//替换的页码下标
		val pageIndex = currentUri.indexOf("page=") + 5;
		//替换的每页条数下标
		val sizeIndex = currentUri.indexOf("size=") + 5;
		//第一页
		links.add(Link.builder()
									.rel(Rel.FIRST)
									.href(currentUri.replace(pageIndex, pageIndex + 1, "0").replace(sizeIndex, sizeIndex + 1, size + "").toString())
									.build());
		//前一页
		if (!page.isFirst())
			links.add(Link.builder()
										.rel(Rel.PREV)
										.href(currentUri.replace(pageIndex, pageIndex + 1, number - 1 + "").replace(sizeIndex, sizeIndex + 1, size + "").toString())
										.build());
		//当前页
		links.add(Link.builder().rel(Rel.SELF).href(currentUri.toString()).build());
		//下一页
		if (!page.isLast())
			links.add(Link.builder()
										.rel(Rel.NEXT)
										.href(currentUri.replace(pageIndex, pageIndex + 1, number + 1 + "").replace(sizeIndex, sizeIndex + 1, size + "").toString())
										.build());
		//最后一页
		val totalPages = page.getTotalPages();
		links.add(Link.builder()
									.rel(Rel.LAST)
									.href(currentUri.replace(pageIndex, pageIndex + 1, totalPages == 0 ? "0" : totalPages - 1 + "")
														.replace(sizeIndex, sizeIndex + 1, size + "")
														.toString())
									.build());

		resource.put("links", links);

		//page
		val pageJson = new JSONObject();
		//是否第一页
		pageJson.put("isFirst", page.isFirst());
		//是否最后一页
		pageJson.put("isLast", page.isLast());
		//当前页码
		pageJson.put("pageNumber", page.getNumber());
		//当前页数据条数
		pageJson.put("pageElements", page.getNumberOfElements());
		//每页条数
		pageJson.put("size", page.getSize());
		//总数据条数
		pageJson.put("totalElements", page.getTotalElements());
		//总页数
		pageJson.put("totalPages", page.getTotalPages());
		resource.put("page", pageJson);

		return Optional.of(resource);
	}

	public static ResponseEntity ok()
	{
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
							 .cacheControl(CacheControl.noCache()).build();
	}

	/**
	 * 资源存在 => 200
	 * 不存在 => 404
	 * @param data
	 * @return
	 */
	public static <T> ResponseEntity ok(final T data)
	{
		return ok(Optional.ofNullable(data));
	}

	public static <T> ResponseEntity ok(final Optional<T> data)
	{
		return data.map(o -> ResponseEntity.ok()
														 .contentType(MediaType.APPLICATION_JSON_UTF8)
														 .cacheControl(CacheControl.noCache())
														 .body(data)
		).orElseGet(() -> ResponseEntity.notFound().cacheControl(CacheControl.noCache()).build());
	}

	/**
	 * 创建资源
	 * @param uq  资源唯一标识
	 * @param obj 资源
	 * @return
	 */
	public static ResponseEntity created(final String uq, final Object... obj)
	{
		try
		{
			val sb = CommonUtil.getStringBuilder();
			//该资源的访问uri
			val uri = sb.append(apiGateway).append(getUri()).append("/").append(uq).toString();

			val response = ResponseEntity.created(new URI(uri))
												 .contentType(MediaType.APPLICATION_JSON_UTF8)
												 .cacheControl(CacheControl.noCache());
			if (obj.length != 0) response.body(obj);
			return response.build();
		}
		catch (URISyntaxException e)
		{
			log.error("创建资源出错!", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * 400请求
	 * @return
	 */
	public static ResponseEntity badRequest()
	{
		return ResponseEntity.badRequest().cacheControl(CacheControl.noCache()).contentType(MediaType.APPLICATION_JSON_UTF8).build();
	}

	/**
	 * 400请求
	 * @param data 返回错误描述
	 * @param <T>
	 * @return
	 */
	public static <T> ResponseEntity badRequest(@NonNull final T data)
	{
		return ResponseEntity.badRequest()
							 .cacheControl(CacheControl.noCache())
							 .contentType(MediaType.APPLICATION_JSON_UTF8)
							 .body(data);
	}

	/**
	 * 400请求
	 * @param code 错误码
	 * @param msg  错误信息
	 * @param <T>
	 * @return
	 */
	public static <T> ResponseEntity badRequest(final int code, final String msg)
	{
		val json = new JSONObject();
		json.put("code", code);
		json.put("msg", msg);
		return ResponseEntity.badRequest()
							 .cacheControl(CacheControl.noCache())
							 .contentType(MediaType.APPLICATION_JSON_UTF8)
							 .body(json);
	}

	/**
	 * 删除资源
	 * @return
	 */
	public static ResponseEntity noContent()
	{
		return ResponseEntity.noContent().cacheControl(CacheControl.noCache()).build();
	}

	/**
	 * 获取当前Uri
	 * @return
	 */
	private static String getUri()
	{
		val request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return request.getRequestURI();
	}

	/**
	 * 获取Http Get请求参数
	 * @return
	 */
	private static String getQueryString()
	{
		val request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return request.getQueryString();
	}
}
