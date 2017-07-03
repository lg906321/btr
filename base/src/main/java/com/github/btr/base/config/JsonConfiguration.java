package com.github.btr.base.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jackson替换成FastJSON
 * Created by ryze on 2017/5/29.
 */
@Configuration
public class JsonConfiguration
{
	@Bean
	public HttpMessageConverters fastJsonHttpMessageConverter()
	{
		//  初始化转换器
		FastJsonHttpMessageConverter4 fastConvert = new FastJsonHttpMessageConverter4();
		//  初始化一个转换器配置
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteDateUseDateFormat,SerializerFeature.PrettyFormat);
		//  将配置设置给转换器并添加到HttpMessageConverter转换器列表中
		fastConvert.setFastJsonConfig(fastJsonConfig);

		return new HttpMessageConverters(fastConvert);
	}
}
