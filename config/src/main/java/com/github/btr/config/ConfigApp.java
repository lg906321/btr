package com.github.btr.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Created by ryze on 2017/7/3.
 */
@EnableConfigServer
@EnableEurekaClient
@SpringBootApplication
public class ConfigApp
{
	public static void main(String[] args)
	{
		SpringApplication.run(ConfigApp.class,args);
	}
}
