package com.github.btr.accounts.interfaces.assembler;

import com.github.btr.accounts.domain.entity.Account;
import com.github.btr.accounts.interfaces.dto.AccountDTO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ryze on 2017/7/3.
 */
public class AccountAssembler
{
	/**
	 * 空DTO
	 * @return
	 */
	public static AccountDTO toEmpty()
	{
		return AccountDTO.builder().build();
	}

	public static AccountDTO toDTO(final Account data)
	{
		/**
		 * 偷懒可以这么写,但是不建议.万一字段名不一样了会导致漏数据.
		 */
//		val dto = new AccountDTO();
//		BeanUtils.copyProperties(data,dto);
		return AccountDTO.builder().id(data.getId()).username(data.getUsername())
							 .password(data.getPassword()).appCode(data.getAppCode()).loginTypeCode(data.getLoginTypeCode()).build();
	}

	public static List<AccountDTO> toDTOs(final List<Account> datas)
	{
		return datas.stream().map(AccountAssembler::toDTO).collect(Collectors.toList());
	}
}
