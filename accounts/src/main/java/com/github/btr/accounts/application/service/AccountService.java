package com.github.btr.accounts.application.service;

import com.github.btr.accounts.interfaces.dto.AccountDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Created by ryze on 2017/7/3.
 */
public interface AccountService
{
	/**
	 * 注册账号
	 * @param dto
	 * @return
	 */
	String register(AccountDTO dto);

	/**
	 * 指定端所有账号
	 * @param appCode
	 * @param pageable
	 * @return
	 */
	Page<AccountDTO> getAppAccounts(int appCode, Pageable pageable);

	/**
	 * 用户信息
	 * @param id
	 * @return
	 */
	Optional<AccountDTO> getAccountInfo(String id);
}
