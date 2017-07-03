package com.github.btr.accounts.domain.cmd;

import lombok.Builder;
import lombok.Value;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

/**
 * 账号命令
 * Created by ryze on 2017/7/3.
 */
public interface AccountCmd
{
	@Value
	@Builder
	final class Register implements AccountCmd
	{
		@TargetAggregateIdentifier
		public final String  id;
		public final String  username;
		public final String  password;
		public final Integer captcha;
		public final int     appCode;
		public final int     loginTypeCode;
	}
}
