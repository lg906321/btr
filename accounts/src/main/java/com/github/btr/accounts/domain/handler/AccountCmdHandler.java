package com.github.btr.accounts.domain.handler;

import com.github.btr.accounts.domain.cmd.AccountCmd;
import com.github.btr.accounts.domain.entity.Account;
import com.github.btr.accounts.infrastructure.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 账号命令处理
 * Created by ryze on 2017/7/3.
 */
@Slf4j
@Component
public class AccountCmdHandler
{
	private final Repository<Account> repository;
	private final AccountRepository accountRepository;

	@Autowired
	public AccountCmdHandler(final Repository<Account> repository, final AccountRepository accountRepository)
	{
		this.repository = repository;
		this.accountRepository = accountRepository;
	}

	@CommandHandler
	public void handle(final AccountCmd.Register cmd)
	{
		log.info("账号注册命令处理");
		//是否已创建
		val accountOp = accountRepository.findAccountByUsernameAndAppCode(cmd.username,cmd.appCode);
		//已创建
		accountOp.map(data ->
		{
			throw new IllegalArgumentException("无效的命令,用户"+data.getUsername()+"已创建!");
			//未创建
		}).orElseGet(() ->
		{
			try
			{
				//账号创建
				return repository.newInstance(() -> new Account(cmd)).invoke(d -> d);
			}
			catch (Exception e)
			{
				log.error("账号创建出错", e);
				throw new IllegalArgumentException("创建账号出错");
			}
		});
	}
}
