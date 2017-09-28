/*
* Project:		
* Author:		赵志武
* Company: 		杭州中软
* Created Date:	2011-08-21
* Copyright @ 2011 CS&S.COM - Confidential and Proprietary
* 
* History:
* ------------------------------------------------------------------------------
* Date			|time		|Author	|Change Description		*/
package com.phantom.plane.core.exception;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.zookeeper.server.quorum.QuorumPeerConfig.ConfigException;

import com.phantom.plane.core.annotation.PhException;
import com.phantom.plane.core.base.BaseException;
import com.phantom.plane.core.common.ZrarLogger;
import com.phantom.plane.core.interfacer.ILogger;




/**
 * 异常处理帮助类(Exception Utils)
 * 
 * @author 赵志武
 * @version 1.0
 * @since jdk1.5
 */
public class ExceptionUtil {
	private static ILogger logger = ZrarLogger.getInstance();
	private static final ExceptionUtil excUtil = new ExceptionUtil();
	
	private ExceptionUtil(){
	}
	
	public static ExceptionUtil getExceptionUtil(){
		return excUtil;
	}
	
	/**
	 * 是否用户自定义异常
	 * @param bean 异常实例
	 * @return
	 */
	public boolean isUserDefinitionException(Object bean){
		return BaseException.class.isAssignableFrom(bean.getClass());
	}
	
	/**
	 * 注册异常方法
	 * @param cause
	 */
	public void registerException(Object cause){
		// 仅注册系统自定义异常
		if(isUserDefinitionException(cause)){
			// 获取异常处理配置
			PhException zrarExc = cause.getClass().getAnnotation(PhException.class);

			ExceptionContext context = ExceptionContext.getContext();
			if(!context.containsException(cause.getClass())){
				// 创建异常定义
				ExceptionDefinition excDefinition = new ExceptionDefinition(String.valueOf(zrarExc.errorCode()));
				// 将抛出的异常及对应的处理定义存储进上下文中
				context.setExceptionDefinition(cause.getClass(), excDefinition);
			}
			try {
				context.addExceptionHandlers(cause.getClass(), zrarExc.handlers());
			} catch (ConfigException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取异常信息
	 * @param e
	 * @param buffer
	 * @return
	 */
	public String getExceptionMsg(Throwable e,StringBuffer buffer){
		try {
			String msg = null;
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw,true);
			e.printStackTrace(pw);
			msg = sw.toString();
			buffer.append(msg);
			pw.close();
			sw.close();
			return msg;
		} catch (IOException e1) {
			logger.error("Zrar: 异常框架输出异常时发生错误！");
		}
		return null;
	}
	
	
	public Throwable getRootCause(Throwable t){
		Throwable base = t;
		int i = 1;
		while(base.getCause() != null && (base != base.getCause() || i++ <=20)){
			base = base.getCause();
		}
		return base;
	}
}
