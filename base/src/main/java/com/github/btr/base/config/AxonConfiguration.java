package com.github.btr.base.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import lombok.val;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.distributed.AnnotationRoutingStrategy;
import org.axonframework.commandhandling.distributed.CommandBusConnector;
import org.axonframework.commandhandling.distributed.CommandRouter;
import org.axonframework.commandhandling.distributed.DistributedCommandBus;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.mongo.eventsourcing.eventstore.DefaultMongoTemplate;
import org.axonframework.mongo.eventsourcing.eventstore.MongoEventStorageEngine;
import org.axonframework.mongo.eventsourcing.eventstore.MongoFactory;
import org.axonframework.mongo.eventsourcing.eventstore.MongoTemplate;
import org.axonframework.mongo.eventsourcing.eventstore.documentperevent.DocumentPerEventStorageStrategy;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.axonframework.springcloud.commandhandling.SpringCloudCommandRouter;
import org.axonframework.springcloud.commandhandling.SpringHttpCommandBusConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

/**
 * Axon设置
 * Created by ryze on 2017/5/31.
 */
@Configuration
public class AxonConfiguration
{
	@Value("${spring.data.mongodb.host}")
	private String     mongoUri;
	@Value("${spring.data.mongodb.database}")
	private String     dbName;
	@Value("${spring.data.mongodb.events.collection.name}")
	private String     eventsCollectionName;
	@Value("${spring.data.mongodb.events.snapshot.collection.name}")
	private String     snapshotCollectionName;
	@Autowired
	private CommandBus commandBus;

	/******************事件存储使用Mongodb******************/
	@Bean
	public JacksonSerializer axonJsonSerializer()
	{
		return new JacksonSerializer();
	}

	@Bean
	public EventStorageEngine eventStorageEngine()
	{
		return new MongoEventStorageEngine(axonJsonSerializer(), null, axonMongoTemplate(), new DocumentPerEventStorageStrategy());
	}

	@Bean(name = "axonMongoTemplate")
	public MongoTemplate axonMongoTemplate()
	{
		MongoTemplate template = new DefaultMongoTemplate(mongoClient(), dbName, eventsCollectionName, snapshotCollectionName);
		return template;
	}

	@Bean
	public MongoClient mongoClient()
	{
		MongoFactory mongoFactory = new MongoFactory();
		mongoFactory.setMongoAddresses(Arrays.asList(new ServerAddress(mongoUri)));
		return mongoFactory.createMongo();
	}

	/******************事件存储使用Mongodb******************/

	@Bean
	public CommandRouter springCloudCommandRouter(DiscoveryClient discoveryClient)
	{
		return new SpringCloudCommandRouter(discoveryClient, new AnnotationRoutingStrategy());
	}

	@Bean
	public CommandBusConnector springHttpCommandBusConnector(RestOperations restOperations, Serializer serializer)
	{
		return new SpringHttpCommandBusConnector(commandBus, restOperations, serializer);
	}

	@Primary
	@Bean
	public DistributedCommandBus springCloudDistributedCommandBus(CommandRouter commandRouter, CommandBusConnector commandBusConnector)
	{
		return new DistributedCommandBus(commandRouter, commandBusConnector);
	}

	@Bean
	public RestTemplate restTemplate()
	{
		val rest        = new RestTemplate();
		val jsonConvert = new FastJsonHttpMessageConverter4();
		// 初始化一个转换器配置
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteDateUseDateFormat, SerializerFeature.PrettyFormat);
		// 将配置设置给转换器并添加到HttpMessageConverter转换器列表中
		jsonConvert.setFastJsonConfig(fastJsonConfig);
		rest.getMessageConverters().add(jsonConvert);
		return rest;
	}
}
