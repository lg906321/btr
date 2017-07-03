package com.github.btr.accounts.interfaces.api.v1;

import com.github.btr.accounts.application.service.AccountService;
import com.github.btr.accounts.interfaces.assembler.AccountAssembler;
import com.github.btr.accounts.interfaces.dto.AccountDTO;
import com.github.btr.base.rest.Href;
import com.github.btr.base.rest.Rel;
import com.github.btr.base.rest.RequestField;
import com.github.btr.base.rest.Restful;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by ryze on 2017/7/3.
 */
@Slf4j
@RestController
@RequestMapping("v2/accounts")
public class AccountApi
{
	private final AccountService accountService;

	@Autowired
	public AccountApi(final AccountService accountService)
	{
		this.accountService = accountService;
	}

	@GetMapping
	public ResponseEntity index()
	{
		log.info("账号资源列表");
		//自身资源
		val resource = Optional.of(AccountAssembler.toEmpty()).map(data -> Restful.addHyperDriven(data, Rel.SELF, null))
											 //账号分页列表
											 .map(data -> Restful.addHyperDriven(data, Rel.COLLECTION, Href.PAGE))
											 //创建账号
											 .map(Restful::create);
		return Restful.ok(resource);

	}

	@PostMapping
	public ResponseEntity register(@RequestBody AccountDTO request)
	{
		log.info("账号注册");
		val id = accountService.register(request);

		return Restful.created(id);
	}

	@GetMapping(params = {RequestField.PAGE, RequestField.SIZE})
	public ResponseEntity list(@RequestParam final Integer page, @RequestParam  final Integer size)
	{
		log.info("账号分页列表");
		val pageable = new PageRequest(page, size);
		val datas    = accountService.getAccounts(pageable);
		//复数资源
		val resources = datas.getContent().stream()
												.map(data -> Restful.toResource(data, true)
																				 //更新删除资源
																				 .map(Restful::ud))
												.collect(Collectors.toList());
		//返回资源
		val resource = Restful.toResources(datas, resources);

		return Restful.ok(resource);
	}
}
