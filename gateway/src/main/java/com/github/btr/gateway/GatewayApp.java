package com.github.btr.gateway;

import lombok.val;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Created by ryze on 2017/7/3.
 */
@EnableZuulProxy
@SpringBootApplication
public class GatewayApp
{
	public static void main(String[] args)
	{
		SpringApplication.run(GatewayApp.class, args);
	}

	/**
	 * 跨域设置 在nginx里面弄了这里就不用了
	 * @return
	 */
	@Bean
	public FilterRegistrationBean corsFilter()
	{
		val source = new UrlBasedCorsConfigurationSource();

		val config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		config.addAllowedOrigin("*");
		source.registerCorsConfiguration("/**", config);
		FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
		bean.setOrder(0);
		return bean;
	}
}
