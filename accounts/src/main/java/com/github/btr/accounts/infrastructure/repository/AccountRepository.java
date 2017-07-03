package com.github.btr.accounts.infrastructure.repository;

import com.github.btr.accounts.domain.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Created by ryze on 2017/7/3.
 */
public interface AccountRepository extends MongoRepository<Account, String>
{
	/**
	 * 账号名和登录端
	 * @param username
	 * @param appCode
	 * @return
	 */
	Optional<Account> findAccountByUsernameAndAppCode(String username, int appCode);

	/**
	 * 指定端的所有账号
	 * @param appCode
	 * @param pageable
	 * @return
	 */
	Page<Account> findAccountsByAppCode(int appCode, Pageable pageable);

	Optional<Account> findAccountById(String id);
}
