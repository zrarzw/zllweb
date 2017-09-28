package com.phantom.plane.core.utils;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * <p>
 * Used to get spring application context
 * 
 * @author GreenZhao
 * 
 */

public class SpringContext implements ApplicationContextAware {
	
	private static Logger log =LoggerFactory.getLogger(SpringContext.class);
	
	private static ApplicationContext context;
	private static final Map<String, Object> serviceMap = new HashMap<String, Object>();
	private static final Map<String, Object> map = new HashMap<String, Object>();
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		SpringContext.context = context;
	}

	public static ApplicationContext getContext() {
		return context;
	}
	
	public static Object getBean(String beanName){
		Object obj = null;
		try {
			obj = context.getBean(beanName);
		} catch (BeansException e) {
			log.error("未获取到bean:"+beanName);
		}
		return obj;
	}
	public static <T> T getBean(Class<T> clazz){
		T obj = null;
		try {
			if(context != null){
				obj = context.getBean(clazz);
			}
		} catch (BeansException e) {
			log.error("未获取到bean:"+clazz.getName());
		}
		return obj;
	}
//	/***
//	 * 置入serviceMap缓存
//	 * @param beanName
//	 * @param bean
//	 */
//	public static void registBeansWithServiceAnnotation(String beanName,Object bean){
//		serviceMap.put(beanName, bean);
//	}
	public static Map<String,Object> getBeansWithServiceAnnotation(){
		return serviceMap;
	}
	public static Object getBeanWithBlhAnnotation(String annotation) {
		return map.get(annotation);
	}
	public static Map<String, Object> getBLHBeansMap() {
		return map;
	}
}
