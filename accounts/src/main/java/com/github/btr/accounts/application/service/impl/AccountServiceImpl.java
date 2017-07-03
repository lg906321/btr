package com.github.btr.accounts.application.service.impl;

import com.github.btr.accounts.application.service.AccountService;
import com.github.btr.accounts.domain.cmd.AccountCmd;
import com.github.btr.accounts.domain.entity.Account;
import com.github.btr.accounts.infrastructure.repository.AccountRepository;
import com.github.btr.accounts.interfaces.assembler.AccountAssembler;
import com.github.btr.accounts.interfaces.dto.AccountDTO;
import lombok.val;
import org.axonframework.commandhandling.callbacks.FutureCallback;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by ryze on 2017/7/3.
 */
@Service
@Transactional
public class AccountServiceImpl implements AccountService
{
	private final CommandGateway    commandGateway;
	private final AccountRepository accountRepository;

	@Autowired
	public AccountServiceImpl(final CommandGateway commandGateway, final AccountRepository accountRepository)
	{
		this.commandGateway = commandGateway;
		this.accountRepository = accountRepository;
	}

	@Override
	public String register(final AccountDTO dto)
	{
		//创建命令
		val cmd = AccountCmd.Register.builder().id(dto.id).username(dto.username).password(dto.password)
									.captcha(dto.captcha).appCode(dto.appCode).loginTypeCode(dto.loginTypeCode).build();
		//异步回调
		val future = new FutureCallback<AccountCmd.Register, Account>();
		//发送命令
		commandGateway.send(cmd, future);
		//命令结果
		val result = future.getResult();
		System.out.println("future =====> " + result);

		return result.getId();
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public Page<AccountDTO> getAccounts(final Pageable pageable)
	{
		val page  = accountRepository.findAccounts(pageable);
		val datas = AccountAssembler.toDTOs(page.getContent());

		return new PageImpl<>(datas, pageable, page.getTotalElements());
	}
}
