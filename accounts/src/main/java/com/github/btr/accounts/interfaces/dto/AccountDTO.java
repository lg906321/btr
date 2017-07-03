package com.github.btr.accounts.interfaces.dto;

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
	//	@NotNull(message = "{account.username.null}", groups = Login.class)
	public final String  username;
	//	@NotNull(message = "{account.password.null}", groups = Login.Password.class)
	public final String  password;
	//	@NotNull(message = "{account.captcha.null}", groups = Login.Captcha.class)
	public final Integer captcha;
	//	@Min(value = 0, message = "{account.app-code.null}", groups = Login.class)
	public final Integer appCode;
	//	@Min(value = 0, message = "{account.login-type-code.null}", groups = Login.class)
	public final Integer loginTypeCode;

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
