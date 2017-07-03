package com.github.btr.base.rest;

import lombok.Builder;
import lombok.Value;

/**
 * Created by ryze on 2017/5/1.
 */
@Value
@Builder
public final class Link
{
	//资源描述符
	public final  String rel;
	//资源uri
	public final String href;
}
