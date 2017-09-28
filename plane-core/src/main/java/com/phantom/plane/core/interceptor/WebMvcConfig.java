package com.phantom.plane.core.interceptor;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.phantom.plane.core.handler.GloExceptionHandler;



/**
 * 向mvc中添加自定义组件 Created by BlueT on 2017/3/9.
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
	@Resource
	private BaseInterceptor baseInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(baseInterceptor).addPathPatterns("/**").excludePathPatterns("/toLogin", "/login",
				"/loginCtrl/login");
		super.addInterceptors(registry);
	}

	/**
	 * 添加静态资源文件，外部可以直接访问地址
	 * 
	 * @param registry
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// registry.addResourceHandler("/upload/**").addResourceLocations("file:"+
		// TaleUtils.getUplodFilePath()+"upload/");
		// registry.addResourceHandler("/WEB-INF/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX+"/WEB-INF/");
		super.addResourceHandlers(registry);
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/toLogin").setViewName("login");
		super.addViewControllers(registry);
	}

	@Override
	public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
		super.configureHandlerExceptionResolvers(exceptionResolvers);
		exceptionResolvers.add(new GloExceptionHandler());

	}

}
