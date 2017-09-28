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

import com.phantom.plane.core.annotation.ZrarException;
import com.phantom.plane.core.base.BaseException;
import com.phantom.plane.core.handler.Log4jHandler;

/**
 * 系统配置异常(Config Exception)
 * 
 * @author 赵志武
 * @version 1.0
 * @since jdk1.5
 * @see BaseException
 */
@ZrarException(errorCode = 2000, handlers = { Log4jHandler.class })
public class ConfigException extends BaseException{
	
	private static final long serialVersionUID = 5808893996142885553L;
	
	public ConfigException(){
		super();
	}
	
	public ConfigException(int errorCode){
		super(errorCode);
	}
	
	public ConfigException(int errorCode, String message){
		super(errorCode, message);
	}
	
	public ConfigException(String message){
		super(message);
	}
	
	public ConfigException(Throwable cause){
		super(cause);
	}
	
	public ConfigException(String message,Throwable cause){
		super(message,cause);
	}
	
	public ConfigException(int errorCode,Throwable cause){
		super(errorCode, cause);
	}
}
