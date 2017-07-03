package com.github.btr.base.rest;

/**
 * 常用资源描述符
 * Created by ryze on 2017/5/1.
 */
public final class Rel
{
	//资源自身
	public static final String SELF       = "self";
	//创建资源
	public static final String CREATE     = "create";
	//查询资源
	public static final String RETRIEVE   = "retrieve";
	//修改资源
	public static final String UPDATE     = "update";
	//删除资源
	public static final String DELETE     = "delete";
	//资源集合
	public static final String COLLECTION = "collection";

	//分页描述符
	public static final String FIRST = "first";
	public static final String PREV  = "prev";
	public static final String NEXT  = "next";
	public static final String LAST  = "last";
}
