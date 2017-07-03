package com.github.btr.accounts.interfaces.dto;

import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;
import com.github.btr.base.rest.Resource;
import lombok.Builder;

/**
 * Created by ryze on 2017/7/3.
 */
@Builder
public class AccountDTO extends Resource
{
	public final String  id;
	public final String  username;
	public final String  password;
	public final Integer captcha;
	public final Integer appCode;
	public final Integer loginTypeCode;

	@JSONCreator
	public AccountDTO(@JSONField(name = "id") final String id, @JSONField(name = "username") final String username,
										@JSONField(name = "password") final String password, @JSONField(name = "captcha") final Integer captcha,
										@JSONField(name = "appCode") final Integer appCode, @JSONField(name = "loginTypeCode") final Integer loginTypeCode)
	{
		this.id = id;
		this.username = username;
		this.password = password;
		this.captcha = captcha;
		this.appCode = appCode;
		this.loginTypeCode = loginTypeCode;
	}
}
