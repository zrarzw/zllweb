/*
* Project:		
* Author:		赵志武
* Company: 		杭州中软
* Created Date:	2011-08-22
* Copyright @ 2011 CS&S.COM - Confidential and Proprietary
* 
* History:
* ------------------------------------------------------------------------------
* Date			|time		|Author	|Change Description		*/
package com.phantom.plane.core.exception;

import com.phantom.plane.core.annotation.ZrarException;
import com.phantom.plane.core.base.BaseException;
import com.phantom.plane.core.handler.ConsoleHandler;

/**
 * 服务异常(Service Exception)
 * 
 * @author 赵志武
 * @version 1.0
 * @since jdk1.5
 * @see BaseException
 */
@ZrarException(errorCode=8000,handlers={ConsoleHandler.class})
public class BusinessException extends BaseException{
	
	private static final long serialVersionUID = 6988953702727636469L;

	public BusinessException(){
		super();
	}
	
	public BusinessException(int errorCode){
		super(errorCode);
	}
	
	public BusinessException(int errorCode, String message){
		super(errorCode, message);
	}
	
	public BusinessException(String message){
		super(message);
	}
	
	public BusinessException(Throwable cause){
		super(cause);
	}
	
	public BusinessException(String message,Throwable cause){
		super(message,cause);
	}
	
	public BusinessException(int errorCode,Throwable cause){
		super(errorCode, cause);
	}
}
