package com.github.btr.accounts.infrastructure.repository;

import com.github.btr.accounts.domain.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Created by ryze on 2017/7/3.
 */
public interface AccountRepository extends MongoRepository<Account,String>
{
	/**
	 * 用户名和登录端
	 * @param username
	 * @param appCode
	 * @return
	 */
	Optional<Account> findAccountByUsernameAndAppCode(String username,int appCode);

	Page<Account> findAccounts(final Pageable pageable);
}
