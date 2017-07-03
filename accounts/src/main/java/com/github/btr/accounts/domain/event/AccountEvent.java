package com.github.btr.accounts.domain.event;

import com.github.btr.accounts.domain.cmd.AccountCmd;
import lombok.Builder;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

/**
 * 账号事件
 * Created by ryze on 2017/7/3.
 */
public interface AccountEvent
{
	@Builder
	final class Registered implements AccountEvent
	{
		@TargetAggregateIdentifier
		public final String            id;
		public final AccountCmd.Register cmd;
	}
}
