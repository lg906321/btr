package com.github.btr.accounts.infrastructure.config;

import com.github.btr.accounts.domain.entity.Account;
import org.axonframework.commandhandling.model.Repository;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 仓库设置
 * Created by ryze on 2017/7/3.
 */
@Configuration
public class RepositoryConfiguration
{
	private final EventStore eventStore;

	@Autowired
	public RepositoryConfiguration(final EventStore eventStore)
	{
		this.eventStore = eventStore;
	}

	@Bean
	public Repository<Account> accountAggregateRepository()
	{
		return new EventSourcingRepository<>(Account.class, eventStore);
	}
}
