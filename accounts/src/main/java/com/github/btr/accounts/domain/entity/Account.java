package com.github.btr.accounts.domain.entity;

import com.github.btr.accounts.domain.cmd.AccountCmd;
import com.github.btr.accounts.domain.event.AccountEvent;
import com.github.btr.base.util.IdWorker;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

/**
 * 账号聚合根
 * Created by ryze on 2017/7/3.
 */
@Data
@Slf4j
@Document
@Aggregate
@NoArgsConstructor
public class Account
{
	@Id
	@AggregateIdentifier
	private String id;
	// 账号
	private String username;
	private String password;
	// 登录端
	private int    appCode;
	// 本次登录方式
	private int    loginTypeCode;
	// 状态码
	private int    statusCode;
	@CreatedDate
	private Date   createTime;
	@LastModifiedDate
	private Date   updateTime;

	public Account(final AccountCmd.Register cmd)
	{
		//唯一主键
		val id = IdWorker.getFlowIdWorkerInstance().nextId() + "";
		//发布账号创建事件
		val event = AccountEvent.Registered.builder().id(id).cmd(cmd).build();
		apply(event);
	}

	@EventSourcingHandler
	public void create(final AccountEvent.Registered event)
	{
		this.id = event.id;
		val cmd = event.cmd;
		this.username = cmd.username;
		this.password = cmd.password;
		this.appCode = cmd.appCode;
		this.loginTypeCode = cmd.loginTypeCode;
		//正常
		this.statusCode = 0;
		this.createTime = new Date();
	}
}
