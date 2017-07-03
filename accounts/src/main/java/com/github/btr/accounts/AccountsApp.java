package com.github.btr.accounts;

import com.github.btr.base.config.AxonConfiguration;
import com.github.btr.base.config.JsonConfiguration;
import com.github.btr.base.config.RedisConfiguration;
import com.github.btr.base.rest.Restful;
import com.github.btr.base.util.RedisUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Created by ryze on 2017/7/3.
 */
@Slf4j
@EnableMongoAuditing
@SpringCloudApplication
@ImportAutoConfiguration(value = {JsonConfiguration.class, RedisConfiguration.class, AxonConfiguration.class})
public class AccountsApp implements CommandLineRunner
{
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Value("${api-gateway}")
	private String              apiGateway;

	public static void main(String[] args)
	{
		SpringApplication.run(AccountsApp.class, args);
	}

	/**
	 * 初始化一些必要数据
	 * @param strings
	 * @throws Exception
	 */
	@Override
	@SneakyThrows(Exception.class)
	public void run(final String... strings)
	{
		//初始化Redis
		RedisUtil.stringRedisTemplate = stringRedisTemplate;
		Restful.apiGateway = apiGateway;
	}
}
