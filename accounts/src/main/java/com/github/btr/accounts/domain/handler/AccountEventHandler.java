package com.github.btr.accounts.domain.handler;

import com.github.btr.accounts.domain.entity.Account;
import com.github.btr.accounts.domain.event.AccountEvent;
import com.github.btr.accounts.infrastructure.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.axonframework.commandhandling.model.Aggregate;
import org.axonframework.commandhandling.model.Repository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 账号事件处理
 * Created by ryze on 2017/7/3.
 */
@Slf4j
@Component
public class AccountEventHandler
{
	private final Repository<Account> repository;
	private final AccountRepository   accountRepository;

	@Autowired
	public AccountEventHandler(final Repository<Account> repository, final AccountRepository accountRepository)
	{
		this.repository = repository;
		this.accountRepository = accountRepository;
	}

	@EventHandler
	public void handle(final AccountEvent.Registered event)
	{
		log.info("账号注册事件处理");
		val aggregate = findOne(event.id);
		aggregate.execute(accountRepository::insert);
	}

	/**
	 * 查找聚合根
	 * @param id
	 * @return
	 */
	private Aggregate<Account> findOne(final String id)
	{
		return repository.load(id);
	}
}
