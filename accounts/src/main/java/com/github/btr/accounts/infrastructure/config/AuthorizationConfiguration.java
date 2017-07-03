package com.github.btr.accounts.infrastructure.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;

/**
 * 授权设置
 * Created by ryze on 2017/6/6.
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationConfiguration extends AuthorizationServerConfigurerAdapter
{
	/**
	 * 自动注入一个授权管理者 自动开启密码授权
	 */
	@Autowired
	private AuthenticationManager authenticationManager;
//	@Autowired
//	private AccountService        accountService;

	private static final String TOKEN_FILE_NAME     = "jwt.jks";
	private static final String TOKEN_FILE_PASSWORD = "oasis_client";
	private static final String TOKEN_FILE_ALIAS    = "jwt";

	/**
	 * Json Web Token
	 * @return
	 */
	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter()
	{
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		ClassPathResource       jwt       = new ClassPathResource(TOKEN_FILE_NAME);
		KeyPair                 keyPair   = new KeyStoreKeyFactory(jwt, TOKEN_FILE_PASSWORD.toCharArray()).getKeyPair(TOKEN_FILE_ALIAS);
		converter.setKeyPair(keyPair);

		return converter;
	}

	@Bean
	public TokenStore tokenStore()
	{
		return new JwtTokenStore(jwtAccessTokenConverter());
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints)
			throws Exception
	{

		endpoints
				.authenticationManager(authenticationManager)
				.tokenStore(tokenStore())
				.accessTokenConverter(jwtAccessTokenConverter());
	}

	@Override
	public void configure(final AuthorizationServerSecurityConfigurer security) throws Exception
	{
		//公开/oauth/token接口
		security.tokenKeyAccess("permitAll()");
		//公开/oauth/check_token接口
		security.checkTokenAccess("permitAll()");
		//允许授权客户端登录
		security.allowFormAuthenticationForClients();
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients)
			throws Exception
	{
		//内存存储客户端类型
		clients.inMemory()
				//客户端名
				.withClient("accounts")
				//授权模式 授权码、客户端、密码
				.authorizedGrantTypes("client_credentials", "refresh_token")
				//客户端拥有权限
				.authorities("role_client")
				//客户端用户拥有权限
				.scopes("server")
				.secret("accounts")
				.accessTokenValiditySeconds(7200)
				.refreshTokenValiditySeconds(7200)
				.and()
				.withClient("commons")
				//授权模式 客户端、密码
				.authorizedGrantTypes("client_credentials","refresh_token")
				//客户端拥有权限
				.authorities("role_client")
				//客户端用户拥有权限
				.scopes("server")
				.secret("commons")
				.autoApprove(true)
				.accessTokenValiditySeconds(7200)
				.refreshTokenValiditySeconds(7200)
				.and()
				.withClient("ui")
				//授权模式 密码
				.authorizedGrantTypes("password","refresh_token")
				//客户端拥有权限
				.authorities("role_client")
				//客户端用户拥有权限
				.scopes("server")
				.secret("ui")
				.autoApprove(true)
				.accessTokenValiditySeconds(7200)
				.refreshTokenValiditySeconds(7200);
	}

	/**
	 * 从数据库读取用户权限
	 */
	@Configuration
	@Order(Ordered.LOWEST_PRECEDENCE - 20)
	protected static class AuthenticationManagerConfiguration extends
			GlobalAuthenticationConfigurerAdapter
	{

		private static final String ADMIN = "ADMIN";

//		@Autowired
//		private AccountService accountService;

		@Override
		public void init(AuthenticationManagerBuilder auth) throws Exception
		{
			//jdbc获取用户权限
//			auth.userDetailsService(accountService);
		}
	}
}
