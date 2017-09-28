/*
* Project:		
* Author:		赵志武
* Company: 		杭州中软
* Created Date:	2011-08-19
* Copyright @ 2011 CS&S.COM - Confidential and Proprietary
* 
* History:
* ------------------------------------------------------------------------------
* Date			|time		|Author	|Change Description		*/
package com.phantom.plane.core.exception;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.zookeeper.server.quorum.QuorumPeerConfig.ConfigException;
import org.springframework.util.Assert;

import com.phantom.plane.core.base.BaseException;
import com.phantom.plane.core.handler.IExceptionHandler;


/**
 * 异常上下文(Exception Context)
 * 
 * @author 赵志武
 * @version 1.0
 * @since jdk1.5
 */
public class ExceptionContext {
	
	// 异常信息Map
	private Map<Class<?>, ExceptionDefinition> exceptionMap;
	// 异常处理器Map
	private Map<String,IExceptionHandler> handler = new HashMap<String, IExceptionHandler>();
	// 异常上下文
	private static final ExceptionContext context = new ExceptionContext();
	
	private ExceptionContext(){
		exceptionMap = new HashMap<Class<?>, ExceptionDefinition>();
	}
	
	// 获取异常上下文
	public static ExceptionContext getContext(){
		return context;
	}
	
	/**
	 * 检查该异常是否已存入上下文
	 * @param clazz
	 * @return
	 */
	public boolean containsException(Class<?> clazz){
		return exceptionMap.containsKey(clazz);
	}
	
	/**
	 * 根据异常获取异常定义对象
	 * @param clazz
	 * @return
	 */
	public ExceptionDefinition getRealExceptionDefine(Class<?> clazz){
		return exceptionMap.get(clazz);
	}
	
	/**
	 * 添加异常所对应的异常定义
	 * @param expClazz
	 * @param definition
	 */
    public void setExceptionDefinition(Class<?> expClazz, ExceptionDefinition definition) {
        exceptionMap.put(expClazz, definition);
    }

    /**
     * 获取异常对应的异常定义
     * @param expClazz
     * @return
     */
    public ExceptionDefinition getExceptionDefinition(Class<?> expClazz) {
        if (containsException(expClazz)) {
            return exceptionMap.get(expClazz);  
        } else if (BaseException.class.isAssignableFrom(expClazz.getSuperclass())) {
            return getExceptionDefinition(expClazz.getSuperclass());
        } else {
            return null;
        }
    }
    
	/**
	 * 添加异常处理器
	 * @param clazz 			异常定义
	 * @param handlerClazz		异常处理器
	 * @throws ConfigException 
	 */
	public void addExceptionHandler(Class<?> clazz,Class<? extends IExceptionHandler> handlerClazz) throws ConfigException{
		try {
			// 异常定义类
			ExceptionDefinition exDefine = getRealExceptionDefine(clazz);
			if(exDefine == null){
				throw new ConfigException("异常上下文中不存在异常定义 [ "+clazz.getName() + ",请检查异常定义配置！");
			}
			// 异常处理类
			IExceptionHandler exHandler = handler.get(handlerClazz);
			if(exHandler == null){
				exHandler =	handlerClazz.newInstance();
				handler.put(handlerClazz.getName(), exHandler);
			}
			// 将异常处理器名添加进异常定义
			exDefine.getHandlerNames().add(handlerClazz.getName());
		} catch (Exception e) {
			throw new ConfigException("异常上下文添加异常处理器错误！",e);
		}
	}
	
	
	/**
	 * 添加异常处理器
	 * @param errorCode	 		错误码
	 * @param clazz				异常定义
	 * @param handlerClazz		异常处理器
	 * @throws ConfigException 
	 */
	public void addExceptionHandler(String errorCode,Class<?> clazz, Class<? extends IExceptionHandler> handlerClazz) throws ConfigException {
        Assert.hasLength(errorCode, clazz + " ERROR CODE不能为空！");
        ExceptionDefinition definition = getRealExceptionDefine(clazz);
        if(null == definition) {
            definition = new ExceptionDefinition(errorCode);
            exceptionMap.put(clazz, definition);
        }
        addExceptionHandler(clazz, handlerClazz);
    }
	
	/**
	 * 添加异常处理器
	 * @param clazz 			异常定义
	 * @param handlerClazzes	异常处理器
	 * @throws ConfigException 
	 */
	public void addExceptionHandlers(Class<?> clazz, Class<? extends IExceptionHandler>... handlerClazzes) throws ConfigException {
        for(Class<? extends IExceptionHandler> handlerClazz : handlerClazzes) {
            addExceptionHandler(clazz, handlerClazz);
        }
    }
	
	
	/**
	 * 移除异常处理器
	 * @param exClazz
	 * @param handlerClazz
	 */
    public void removeExceptionHandler(Class<?> exClazz, Class<? extends IExceptionHandler> handlerClazz) {
        Assert.isTrue(containsException(exClazz));
        
        String handlerName = handlerClazz.getName();
        // 删除异常处理类中处理器
        getExceptionDefinition(exClazz).getHandlerNames().remove(handlerName);
        
        Collection<ExceptionDefinition> definitons = exceptionMap.values();
        boolean isClearHandler = true;
        for (ExceptionDefinition expDefinition : definitons) {
            if (expDefinition.getHandlerNames().contains(handlerName)) {
                isClearHandler = false;
                break;
            }
        }
        if (isClearHandler) {
            handler.remove(handler.get(handlerName));
        }
    }
	
    /**
     * 获取异常处理器集合
     * @param expClazz
     * @return
     */
    public List<IExceptionHandler> getExceptionHandlers(Class<?> expClazz){
        ExceptionDefinition definition = getExceptionDefinition(expClazz);
        
        if (null != definition) {
            Set<String> handlerNames = definition.getHandlerNames();
            List<IExceptionHandler> handlerList = new ArrayList<IExceptionHandler>(handlerNames.size());
            for (String handlerName : handlerNames) {
                IExceptionHandler exHandler = handler.get(handlerName);
                handlerList.add(exHandler);
            }
            List<IExceptionHandler> resultHandlerList = new ArrayList<IExceptionHandler>(handlerList);
            return resultHandlerList;
        } else {
            return Collections.<IExceptionHandler> emptyList();
        }
    }
    
    
    /**
     * 获取错误码
     * @param expClazz
     * @return
     */
    public String getErrorCode(Class<?> expClazz){
        ExceptionDefinition definition = getExceptionDefinition(expClazz);
        if (null != definition) {
            return definition.getErrorCode();
        } else {
            return "";
        }
    }
}
