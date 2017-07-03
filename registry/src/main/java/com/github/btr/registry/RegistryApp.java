package com.github.btr.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Created by ryze on 2017/7/3.
 */
@EnableEurekaServer
@SpringBootApplication
public class RegistryApp
{
	public static void main(String[] args)
	{
		SpringApplication.run(RegistryApp.class,args);
	}
}
