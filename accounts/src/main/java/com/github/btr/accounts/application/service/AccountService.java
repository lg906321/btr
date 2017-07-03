package com.github.btr.accounts.application.service;

import com.github.btr.accounts.interfaces.dto.AccountDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by ryze on 2017/7/3.
 */
public interface AccountService
{
	String register(final AccountDTO dto);

	Page<AccountDTO> getAccounts(final Pageable pageable);
}
