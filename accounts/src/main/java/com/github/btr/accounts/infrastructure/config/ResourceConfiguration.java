package com.github.btr.accounts.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * 资源访问设置
 * Created by ryze on 2017/5/28.
 */
@Configuration
@EnableResourceServer
public class ResourceConfiguration extends ResourceServerConfigurerAdapter
{
	@Override
	public void configure(final HttpSecurity http) throws Exception
	{
		http
				//禁用Basic校验
				.httpBasic().disable()
				//禁用CsrfToken校验
				.csrf().disable()
				//Session无状态性
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				//禁用Header缓存
				.headers().cacheControl().disable().and()
				//暂时一切资源都不鉴权
				.authorizeRequests().anyRequest().permitAll();
	}

	@Override
	public void configure(final ResourceServerSecurityConfigurer resources) throws Exception
	{
		resources.resourceId("accounts");
	}
}